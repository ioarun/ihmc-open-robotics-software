package us.ihmc.avatar;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;

import us.ihmc.avatar.drcRobot.DRCRobotModel;
import us.ihmc.avatar.initialSetup.OffsetAndYawRobotInitialSetup;
import us.ihmc.avatar.testTools.DRCSimulationTestHelper;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple4D.Quaternion;
import us.ihmc.humanoidRobotics.communication.packets.walking.FootstepDataListMessage;
import us.ihmc.humanoidRobotics.communication.packets.walking.FootstepDataMessage;
import us.ihmc.humanoidRobotics.communication.packets.walking.PauseWalkingMessage;
import us.ihmc.robotModels.FullHumanoidRobotModel;
import us.ihmc.robotics.robotSide.RobotSide;
import us.ihmc.simulationConstructionSetTools.bambooTools.BambooTools;
import us.ihmc.simulationConstructionSetTools.util.environments.FlatGroundEnvironment;
import us.ihmc.simulationconstructionset.util.simulationRunner.BlockingSimulationRunner.SimulationExceededMaximumTimeException;
import us.ihmc.simulationconstructionset.util.simulationTesting.SimulationTestingParameters;
import us.ihmc.tools.MemoryTools;
import us.ihmc.tools.thread.ThreadTools;

public abstract class AvatarPauseWalkingTest implements MultiRobotTestInterface
{
   private SimulationTestingParameters simulationTestingParameters = new SimulationTestingParameters();
   private DRCSimulationTestHelper drcSimulationTestHelper;
   private DRCRobotModel robotModel;
   private FullHumanoidRobotModel fullRobotModel;
   
   public abstract boolean keepSCSUp();
   public abstract double getSwingTime();
   public abstract double getTransferTime();
   public abstract double getStepLength();
   public abstract double getStepWidth();
   public abstract double getTimeForPausing();
   public abstract double getTimeForResuming();
   public abstract int getNumberOfFoosteps();
   
   @Before
   public void showMemoryUsageBeforeTest()
   {
      MemoryTools.printCurrentMemoryUsageAndReturnUsedMemoryInMB(getClass().getSimpleName() + " before test.");
      BambooTools.reportTestStartedMessage(simulationTestingParameters.getShowWindows());
   }

   @After
   public void destroySimulationAndRecycleMemory()
   {
      if (simulationTestingParameters.getKeepSCSUp())
      {
         ThreadTools.sleepForever();
      }

      // Do this here in case a test fails. That way the memory will be recycled.
      if (drcSimulationTestHelper != null)
      {
         drcSimulationTestHelper.destroySimulation();
         drcSimulationTestHelper = null;
      }
      robotModel = null;
      fullRobotModel = null;
      
      MemoryTools.printCurrentMemoryUsageAndReturnUsedMemoryInMB(getClass().getSimpleName() + " after test.");
      BambooTools.reportTestFinishedMessage(simulationTestingParameters.getShowWindows());
   }

   public void testPauseWalking() throws SimulationExceededMaximumTimeException
   {
      setupTest();
      assertTrue(drcSimulationTestHelper.simulateAndBlockAndCatchExceptions(1.0));
      sendFootstepCommand(getNumberOfFoosteps());
      assertTrue(drcSimulationTestHelper.simulateAndBlockAndCatchExceptions(getTimeForPausing()));
      PauseWalkingMessage pauseWalkingMessage = new PauseWalkingMessage(true);
      drcSimulationTestHelper.send(pauseWalkingMessage);
      assertTrue(drcSimulationTestHelper.simulateAndBlockAndCatchExceptions(getTimeForResuming()));
      pauseWalkingMessage = new PauseWalkingMessage(false);
      drcSimulationTestHelper.send(pauseWalkingMessage);
      assertTrue(drcSimulationTestHelper.simulateAndBlockAndCatchExceptions(getNumberOfFoosteps() * (getSwingTime() + getTransferTime())));
   }
   
   private void sendFootstepCommand(int numberOfFootsteps)
   {
      FootstepDataListMessage footstepMessage = new FootstepDataListMessage(getSwingTime(), getTransferTime());
      RobotSide side = RobotSide.LEFT;
      Quaternion orientation = new Quaternion();
      for(int i = 1; i < numberOfFootsteps; i++)
      {
         addFootstep(new Point3D(i * getStepLength(), side.negateIfRightSide(getStepWidth()/2.0), 0.0), orientation, side, footstepMessage);
         side = side.getOppositeSide();
      }
      addFootstep(new Point3D((numberOfFootsteps - 1) * getStepLength(), side.negateIfRightSide(getStepWidth()/2.0), 0.0), orientation, side, footstepMessage);
      drcSimulationTestHelper.send(footstepMessage);
   }
   
   private void addFootstep(Point3D stepLocation, Quaternion orient, RobotSide robotSide, FootstepDataListMessage message)
   {
      FootstepDataMessage footstepData = new FootstepDataMessage();
      footstepData.setLocation(stepLocation);
      footstepData.setOrientation(orient);
      footstepData.setRobotSide(robotSide);
      message.add(footstepData);
   }

   private void setupTest()
   {
      BambooTools.reportTestStartedMessage(simulationTestingParameters.getShowWindows());
      FlatGroundEnvironment emptyEnvironment = new FlatGroundEnvironment();
      String className = getClass().getSimpleName();
      
      DRCStartingLocation startingLocation = new DRCStartingLocation()
      {
         @Override
         public OffsetAndYawRobotInitialSetup getStartingLocationOffset()
         {
            return new OffsetAndYawRobotInitialSetup(new Vector3D(0.0, 0.0, 0.0), 0.0);
         }
      };
      robotModel = getRobotModel();
      fullRobotModel = robotModel.createFullRobotModel();
      simulationTestingParameters.setKeepSCSUp(keepSCSUp());
      drcSimulationTestHelper = new DRCSimulationTestHelper(simulationTestingParameters, robotModel);
      drcSimulationTestHelper.setStartingLocation(startingLocation);
      drcSimulationTestHelper.setTestEnvironment(emptyEnvironment);
      drcSimulationTestHelper.createSimulation(className);
      ThreadTools.sleep(1000);
      setupCameraSideView();
   }
   
   private void setupCameraSideView()
   {
      Point3D cameraFix = new Point3D(0.0, 0.0, 1.0);
      Point3D cameraPosition = new Point3D(0.0, 10.0, 1.0);
      drcSimulationTestHelper.setupCameraForUnitTest(cameraFix, cameraPosition);
   }


   
}
