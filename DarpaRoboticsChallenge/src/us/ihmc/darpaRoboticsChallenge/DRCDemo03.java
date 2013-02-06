package us.ihmc.darpaRoboticsChallenge;

import java.util.List;

import javax.media.j3d.Transform3D;
import javax.vecmath.Point2d;

import us.ihmc.SdfLoader.SDFRobot;
import us.ihmc.commonWalkingControlModules.automaticSimulationRunner.AutomaticSimulationRunner;
import us.ihmc.commonWalkingControlModules.controllers.ControllerFactory;
import us.ihmc.commonWalkingControlModules.highLevelHumanoidControl.DrivingHighLevelHumanoidControllerFactory;
import us.ihmc.darpaRoboticsChallenge.controllers.DRCRobotMomentumBasedControllerFactory;
import us.ihmc.darpaRoboticsChallenge.drcRobot.DRCRobotJointMap;
import us.ihmc.darpaRoboticsChallenge.drcRobot.DRCRobotParameters;
import us.ihmc.darpaRoboticsChallenge.initialSetup.DrivingDRCRobotInitialSetup;
import us.ihmc.projectM.R2Sim02.initialSetup.RobotInitialSetup;
import us.ihmc.robotSide.RobotSide;
import us.ihmc.robotSide.SideDependentList;

import com.martiansoftware.jsap.JSAPException;
import com.yobotics.simulationconstructionset.SimulationConstructionSet;
import com.yobotics.simulationconstructionset.util.graphics.DynamicGraphicObjectsListRegistry;

public class DRCDemo03
{
   private final HumanoidRobotSimulation<SDFRobot> drcSimulation;
   private final DRCDemoEnvironmentWithBoxAndSteeringWheel environment;

   public DRCDemo03(DRCRobotModel robotModel,
         DRCGuiInitialSetup guiInitialSetup, AutomaticSimulationRunner automaticSimulationRunner, double timePerRecordTick,
         int simulationDataBufferSize, String ipAddress, int portNumber)
   {
      DRCSCSInitialSetup scsInitialSetup;
      RobotInitialSetup<SDFRobot> robotInitialSetup = new DrivingDRCRobotInitialSetup();

      environment = new DRCDemoEnvironmentWithBoxAndSteeringWheel();
      scsInitialSetup = new DRCSCSInitialSetup(environment);
      scsInitialSetup.setSimulationDataBufferSize(simulationDataBufferSize);

      double dt = scsInitialSetup.getDT();
      int recordFrequency = (int) Math.round(timePerRecordTick / dt);
      if (recordFrequency < 1)
         recordFrequency = 1;
      scsInitialSetup.setRecordFrequency(recordFrequency);

      DynamicGraphicObjectsListRegistry dynamicGraphicObjectsListRegistry = new DynamicGraphicObjectsListRegistry();
     
      DRCRobotJointMap jointMap = new DRCRobotJointMap(robotModel);
      
//      SideDependentList<ContactablePlaneBody> thighs = new SideDependentList<ContactablePlaneBody>();
//      InverseDynamicsJoint[] allJoints = ScrewTools.computeJointsInOrder(fullRobotModel.getElevator());
      
      
      SideDependentList<String> namesOfJointsBeforeThighs = new SideDependentList<String>();
      SideDependentList<Transform3D> thighContactPointTransforms = new SideDependentList<Transform3D>();
      SideDependentList<List<Point2d>> thighContactPoints = new SideDependentList<List<Point2d>>();
      for (RobotSide robotSide : RobotSide.values())
      {
         namesOfJointsBeforeThighs.put(robotSide, jointMap.getNameOfJointBeforeThigh(robotSide));
         thighContactPointTransforms.put(robotSide, DRCRobotParameters.thighContactPointTransforms.get(robotSide));
         thighContactPoints.put(robotSide, DRCRobotParameters.thighContactPoints.get(robotSide));
      }
      
      
      DrivingHighLevelHumanoidControllerFactory highLevelHumanoidControllerFactory = new DrivingHighLevelHumanoidControllerFactory(namesOfJointsBeforeThighs, thighContactPointTransforms, thighContactPoints);
      ControllerFactory controllerFactory = new DRCRobotMomentumBasedControllerFactory(highLevelHumanoidControllerFactory, true);

      drcSimulation = DRCSimulationFactory.createSimulation(jointMap, controllerFactory, environment, robotInitialSetup, scsInitialSetup, guiInitialSetup);

      SimulationConstructionSet simulationConstructionSet = drcSimulation.getSimulationConstructionSet();

      // add other registries
      drcSimulation.addAdditionalDynamicGraphicObjectsListRegistries(dynamicGraphicObjectsListRegistry);

      simulationConstructionSet.setCameraPosition(6.0, -2.0, 4.5);
      simulationConstructionSet.setCameraFix(-0.44, -0.17, 0.75);

      if (automaticSimulationRunner != null)
      {
         drcSimulation.start(automaticSimulationRunner);
      }
      else
      {
         drcSimulation.start(null);
      }
   }

   public static void main(String[] args) throws JSAPException
   {
      AutomaticSimulationRunner automaticSimulationRunner = null;

      DRCGuiInitialSetup guiInitialSetup = new DRCGuiInitialSetup();

      double timePerRecordTick = 0.005;
      int simulationDataBufferSize = 16000;
      String ipAddress = null;
      int portNumber = -1;
      new DRCDemo03(DRCRobotModel.ATLAS_NO_HANDS, guiInitialSetup, automaticSimulationRunner, timePerRecordTick, simulationDataBufferSize, ipAddress,
                                     portNumber);
   }

   public SimulationConstructionSet getSimulationConstructionSet()
   {
      return drcSimulation.getSimulationConstructionSet();
   }
}
