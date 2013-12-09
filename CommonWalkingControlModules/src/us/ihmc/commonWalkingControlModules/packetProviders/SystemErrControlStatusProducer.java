package us.ihmc.commonWalkingControlModules.packetProviders;

import us.ihmc.robotSide.RobotSide;

public class SystemErrControlStatusProducer implements ControlStatusProducer
{

   public void notifyHandTrajectoryInfeasible(RobotSide robotSide)
   {
      System.err.println(robotSide.getCamelCaseNameForStartOfExpression() + " hand trajectory is infeasible");
   }

}
