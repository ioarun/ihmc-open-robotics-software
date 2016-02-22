package us.ihmc.commonWalkingControlModules.highLevelHumanoidControl.factories;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import us.ihmc.SdfLoader.models.FullHumanoidRobotModel;
import us.ihmc.commonWalkingControlModules.configurations.WalkingControllerParameters;
import us.ihmc.commonWalkingControlModules.controllers.Updatable;
import us.ihmc.commonWalkingControlModules.desiredFootStep.AbortWalkingProvider;
import us.ihmc.commonWalkingControlModules.desiredFootStep.BlindWalkingPacketConsumer;
import us.ihmc.commonWalkingControlModules.desiredFootStep.BlindWalkingToDestinationDesiredFootstepCalculator;
import us.ihmc.commonWalkingControlModules.desiredFootStep.FootstepPathConsumer;
import us.ihmc.commonWalkingControlModules.desiredFootStep.FootstepPathCoordinator;
import us.ihmc.commonWalkingControlModules.desiredFootStep.FootstepTimingParameters;
import us.ihmc.commonWalkingControlModules.desiredFootStep.PauseWalkingMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.ArmDesiredAccelerationsMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.ArmTrajectoryMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.AutomaticManipulationAbortCommunicator;
import us.ihmc.commonWalkingControlModules.packetConsumers.ChestTrajectoryMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.DesiredChestOrientationProvider;
import us.ihmc.commonWalkingControlModules.packetConsumers.DesiredComHeightProvider;
import us.ihmc.commonWalkingControlModules.packetConsumers.DesiredHeadOrientationProvider;
import us.ihmc.commonWalkingControlModules.packetConsumers.DesiredPelvisPoseProvider;
import us.ihmc.commonWalkingControlModules.packetConsumers.EndEffectorLoadBearingMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.FootTrajectoryMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.HandComplianceControlParametersSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.HandTrajectoryMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.HeadTrajectoryMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.PelvisHeightTrajectoryMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.PelvisTrajectoryMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.StopAllTrajectoryMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetConsumers.WholeBodyTrajectoryMessageSubscriber;
import us.ihmc.commonWalkingControlModules.packetProducers.CapturabilityBasedStatusProducer;
import us.ihmc.commonWalkingControlModules.packetProducers.HandPoseStatusProducer;
import us.ihmc.commonWalkingControlModules.packetProviders.ControlStatusProducer;
import us.ihmc.commonWalkingControlModules.packetProviders.DesiredHighLevelStateProvider;
import us.ihmc.commonWalkingControlModules.packetProviders.NetworkControlStatusProducer;
import us.ihmc.commonWalkingControlModules.trajectories.ConstantSwingTimeCalculator;
import us.ihmc.commonWalkingControlModules.trajectories.ConstantTransferTimeCalculator;
import us.ihmc.humanoidRobotics.bipedSupportPolygons.ContactablePlaneBody;
import us.ihmc.humanoidRobotics.communication.packets.HighLevelStatePacket;
import us.ihmc.humanoidRobotics.communication.packets.manipulation.HandComplianceControlParametersPacket;
import us.ihmc.humanoidRobotics.communication.packets.sensing.LookAtPacket;
import us.ihmc.humanoidRobotics.communication.packets.walking.AbortWalkingPacket;
import us.ihmc.humanoidRobotics.communication.packets.walking.AutomaticManipulationAbortPacket;
import us.ihmc.humanoidRobotics.communication.packets.walking.BlindWalkingPacket;
import us.ihmc.humanoidRobotics.communication.packets.walking.ChestOrientationPacket;
import us.ihmc.humanoidRobotics.communication.packets.walking.ComHeightPacket;
import us.ihmc.humanoidRobotics.communication.packets.walking.FootstepDataListMessage;
import us.ihmc.humanoidRobotics.communication.packets.walking.HeadOrientationPacket;
import us.ihmc.humanoidRobotics.communication.packets.walking.PauseWalkingMessage;
import us.ihmc.humanoidRobotics.communication.packets.walking.PelvisPosePacket;
import us.ihmc.humanoidRobotics.communication.streamingData.HumanoidGlobalDataProducer;
import us.ihmc.humanoidRobotics.footstep.Footstep;
import us.ihmc.robotics.dataStructures.registry.YoVariableRegistry;
import us.ihmc.robotics.dataStructures.variable.DoubleYoVariable;
import us.ihmc.robotics.robotSide.SideDependentList;
import us.ihmc.robotics.trajectories.providers.TrajectoryParameters;
import us.ihmc.sensorProcessing.frames.CommonHumanoidReferenceFrames;
import us.ihmc.simulationconstructionset.yoUtilities.graphics.YoGraphicsListRegistry;
import us.ihmc.tools.thread.CloseableAndDisposableRegistry;
import us.ihmc.util.PeriodicThreadScheduler;

public class DataProducerVariousWalkingProviderFactory implements VariousWalkingProviderFactory
{
   private final HumanoidGlobalDataProducer objectCommunicator;
   private final FootstepTimingParameters footstepTimingParameters;
   private final PeriodicThreadScheduler scheduler;

   public DataProducerVariousWalkingProviderFactory(HumanoidGlobalDataProducer objectCommunicator, FootstepTimingParameters footstepTimingParameters,
         PeriodicThreadScheduler scheduler)
   {
      this.objectCommunicator = objectCommunicator;
      this.footstepTimingParameters = footstepTimingParameters;
      this.scheduler = scheduler;
   }

   public VariousWalkingProviders createVariousWalkingProviders(DoubleYoVariable yoTime, FullHumanoidRobotModel fullRobotModel,
         WalkingControllerParameters walkingControllerParameters, CommonHumanoidReferenceFrames referenceFrames, SideDependentList<? extends ContactablePlaneBody> feet,
         ConstantTransferTimeCalculator transferTimeCalculator, ConstantSwingTimeCalculator swingTimeCalculator, ArrayList<Updatable> updatables,
         YoVariableRegistry registry, YoGraphicsListRegistry yoGraphicsListRegistry, CloseableAndDisposableRegistry closeableAndDisposeableRegistry)
   {

      HandTrajectoryMessageSubscriber handTrajectoryMessageSubscriber = new HandTrajectoryMessageSubscriber(objectCommunicator);
      ArmTrajectoryMessageSubscriber armTrajectoryMessageSubscriber = new ArmTrajectoryMessageSubscriber(objectCommunicator);
      ArmDesiredAccelerationsMessageSubscriber armDesiredAccelerationsMessageSubscriber = new ArmDesiredAccelerationsMessageSubscriber(objectCommunicator);
      HeadTrajectoryMessageSubscriber headTrajectoryMessageSubscriber = new HeadTrajectoryMessageSubscriber(objectCommunicator);
      ChestTrajectoryMessageSubscriber chestTrajectoryMessageSubscriber = new ChestTrajectoryMessageSubscriber(objectCommunicator);
      PelvisTrajectoryMessageSubscriber pelvisTrajectoryMessageSubscriber = new PelvisTrajectoryMessageSubscriber(objectCommunicator);
      FootTrajectoryMessageSubscriber footTrajectoryMessageSubscriber = new FootTrajectoryMessageSubscriber(objectCommunicator);
      EndEffectorLoadBearingMessageSubscriber endEffectorLoadBearingMessageSubscriber = new EndEffectorLoadBearingMessageSubscriber(objectCommunicator);
      StopAllTrajectoryMessageSubscriber stopAllTrajectoryMessageSubscriber = new StopAllTrajectoryMessageSubscriber(objectCommunicator);
      PelvisHeightTrajectoryMessageSubscriber pelvisHeightTrajectoryMessageSubscriber = new PelvisHeightTrajectoryMessageSubscriber(objectCommunicator);

      // This guy will redirect the messages contained in the WholeBodyTrajectoryMessage to the other subscribers. No need to hold on it.
      new WholeBodyTrajectoryMessageSubscriber(handTrajectoryMessageSubscriber, armTrajectoryMessageSubscriber, chestTrajectoryMessageSubscriber,
            pelvisTrajectoryMessageSubscriber, footTrajectoryMessageSubscriber, objectCommunicator);

      AutomaticManipulationAbortCommunicator automaticManipulationAbortCommunicator = new AutomaticManipulationAbortCommunicator(objectCommunicator);

      HandComplianceControlParametersSubscriber handComplianceControlParametersSubscriber = new HandComplianceControlParametersSubscriber();

      HandPoseStatusProducer handPoseStatusProducer = new HandPoseStatusProducer(objectCommunicator);

      LinkedHashMap<Footstep, TrajectoryParameters> mapFromFootstepsToTrajectoryParameters = new LinkedHashMap<Footstep, TrajectoryParameters>();

      BlindWalkingToDestinationDesiredFootstepCalculator desiredFootstepCalculator = HighLevelHumanoidControllerFactoryHelper
            .getBlindWalkingToDestinationDesiredFootstepCalculator(walkingControllerParameters, referenceFrames, feet, registry);

      CapturabilityBasedStatusProducer capturabilityBasedStatusProducer = new CapturabilityBasedStatusProducer(closeableAndDisposeableRegistry, scheduler,
            objectCommunicator);

      FootstepPathCoordinator footstepPathCoordinator = new FootstepPathCoordinator(footstepTimingParameters, objectCommunicator, desiredFootstepCalculator,
            swingTimeCalculator, transferTimeCalculator, registry);

      FootstepPathConsumer footstepPathConsumer = new FootstepPathConsumer(feet, footstepPathCoordinator, mapFromFootstepsToTrajectoryParameters,
            objectCommunicator);
      BlindWalkingPacketConsumer blindWalkingPacketConsumer = new BlindWalkingPacketConsumer(footstepPathCoordinator);
      PauseWalkingMessageSubscriber pauseWalkingMessageSubscriber = new PauseWalkingMessageSubscriber(footstepPathCoordinator);
      DesiredHighLevelStateProvider highLevelStateProvider = new DesiredHighLevelStateProvider();
      double trajectoryTimeHeadOrientation = walkingControllerParameters.getTrajectoryTimeHeadOrientation();
      DesiredHeadOrientationProvider headOrientationProvider = new DesiredHeadOrientationProvider(fullRobotModel.getChest().getBodyFixedFrame(),
            trajectoryTimeHeadOrientation, objectCommunicator);
      DesiredComHeightProvider desiredComHeightProvider = new DesiredComHeightProvider(objectCommunicator);
      DesiredPelvisPoseProvider pelvisPoseProvider = new DesiredPelvisPoseProvider();
      DesiredChestOrientationProvider chestOrientationProvider = new DesiredChestOrientationProvider(trajectoryTimeHeadOrientation, objectCommunicator);

      AbortWalkingProvider abortWalkingProvider = new AbortWalkingProvider();

      objectCommunicator.attachListener(FootstepDataListMessage.class, footstepPathConsumer);
      objectCommunicator.attachListener(BlindWalkingPacket.class, blindWalkingPacketConsumer);
      objectCommunicator.attachListener(PauseWalkingMessage.class, pauseWalkingMessageSubscriber);
      objectCommunicator.attachListener(HighLevelStatePacket.class, highLevelStateProvider);
      objectCommunicator.attachListener(HeadOrientationPacket.class, headOrientationProvider.getHeadOrientationPacketConsumer());
      objectCommunicator.attachListener(ComHeightPacket.class, desiredComHeightProvider);
      objectCommunicator.attachListener(LookAtPacket.class, headOrientationProvider.getLookAtPacketConsumer());
      objectCommunicator.attachListener(AutomaticManipulationAbortPacket.class, automaticManipulationAbortCommunicator);
      objectCommunicator.attachListener(ChestOrientationPacket.class, chestOrientationProvider);
      objectCommunicator.attachListener(PelvisPosePacket.class, pelvisPoseProvider);
      objectCommunicator.attachListener(HandComplianceControlParametersPacket.class, handComplianceControlParametersSubscriber);
      objectCommunicator.attachListener(AbortWalkingPacket.class, abortWalkingProvider);

      ControlStatusProducer controlStatusProducer = new NetworkControlStatusProducer(objectCommunicator);

      VariousWalkingProviders variousWalkingProviders = new VariousWalkingProviders(handTrajectoryMessageSubscriber, armTrajectoryMessageSubscriber,
            armDesiredAccelerationsMessageSubscriber, headTrajectoryMessageSubscriber, chestTrajectoryMessageSubscriber, pelvisTrajectoryMessageSubscriber,
            footTrajectoryMessageSubscriber, endEffectorLoadBearingMessageSubscriber, stopAllTrajectoryMessageSubscriber,
            pelvisHeightTrajectoryMessageSubscriber, footstepPathCoordinator, headOrientationProvider, desiredComHeightProvider, pelvisPoseProvider,
            handComplianceControlParametersSubscriber, automaticManipulationAbortCommunicator, chestOrientationProvider, highLevelStateProvider, controlStatusProducer,
            capturabilityBasedStatusProducer, handPoseStatusProducer, abortWalkingProvider);

      return variousWalkingProviders;
   }

}
