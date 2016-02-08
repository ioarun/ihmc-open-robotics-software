package us.ihmc.robotiq.control;

import java.io.IOException;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;

import us.ihmc.commonWalkingControlModules.packetConsumers.HandDesiredConfigurationSubscriber;
import us.ihmc.darpaRoboticsChallenge.handControl.HandControlThread;
import us.ihmc.darpaRoboticsChallenge.handControl.packetsAndConsumers.HandJointAngleCommunicator;
import us.ihmc.darpaRoboticsChallenge.handControl.packetsAndConsumers.ManualHandControlProvider;
import us.ihmc.humanoidRobotics.communication.packets.dataobjects.HandConfiguration;
import us.ihmc.humanoidRobotics.communication.packets.manipulation.HandDesiredConfigurationMessage;
import us.ihmc.humanoidRobotics.communication.packets.manipulation.ManualHandControlPacket;
import us.ihmc.robotics.robotSide.RobotSide;
import us.ihmc.robotiq.RobotiqHandCommunicator;
import us.ihmc.robotiq.data.RobotiqHandSensorDizzata;
import us.ihmc.tools.thread.CloseableAndDisposableRegistry;
import us.ihmc.tools.thread.ThreadTools;

public class RobotiqControlThrizzead extends HandControlThread
{
   private final boolean CALIBRATE_ON_CONNECT = false;
   
   private final RobotSide robotSide;
   private final RobotiqHandCommunicator robotiqHand;
   private final HandDesiredConfigurationSubscriber handDesiredConfigurationSubscriber;
   private final ManualHandControlProvider manualHandControlProvider;
   private final HandJointAngleCommunicator jointAngleCommunicator;
   private RobotiqHandSensorDizzata handStatus;

   public RobotiqControlThrizzead(RobotSide robotSide, CloseableAndDisposableRegistry closeableAndDisposableRegistry)
   {
      super(robotSide);
      this.robotSide = robotSide;
      robotiqHand = new RobotiqHandCommunicator(robotSide);
      handDesiredConfigurationSubscriber = new HandDesiredConfigurationSubscriber(robotSide);
      manualHandControlProvider = new ManualHandControlProvider(robotSide);
      jointAngleCommunicator = new HandJointAngleCommunicator(robotSide, packetCommunicator, closeableAndDisposableRegistry);
      
      packetCommunicator.attachListener(HandDesiredConfigurationMessage.class, handDesiredConfigurationSubscriber);
      packetCommunicator.attachListener(ManualHandControlPacket.class, manualHandControlProvider);
   }

   public void connect()
   {
      try
      {
         packetCommunicator.connect();
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      
      while(!packetCommunicator.isConnected())
      {
         ThreadTools.sleep(10);
      }
   }

   private void updateHandData()
   {
      handStatus = robotiqHand.getHandSensorData();
      jointAngleCommunicator.updateHandAngles(handStatus);
      jointAngleCommunicator.write();
   }

   public void run()
   {
      if(CALIBRATE_ON_CONNECT)
         handDesiredConfigurationSubscriber.receivedPacket(new HandDesiredConfigurationMessage(robotSide, HandConfiguration.CALIBRATE));

      while (packetCommunicator.isConnected())
      {
         robotiqHand.read();
         
         updateHandData();

         if (handStatus.hasError())
         {
//            System.out.println(handStatus.getFaultStatus().name());
         }
         
         if (handDesiredConfigurationSubscriber.isNewDesiredConfigurationAvailable())
         {
            HandDesiredConfigurationMessage packet = handDesiredConfigurationSubscriber.pullMessage();
            HandConfiguration state = packet.getHandDesiredConfiguration();
            
            switch (state)
            {
            case CALIBRATE:
               robotiqHand.initialize();
               break;
            case OPEN:
            case CLOSE:
            case CRUSH:
            case BASIC_GRIP:
            case PINCH_GRIP:
            case WIDE_GRIP:
            case SCISSOR_GRIP:
               if(robotiqHand.isConnected())
                  robotiqHand.sendHandCommand(state);
               break;
            case OPEN_FINGERS:
            case CLOSE_FINGERS:
               if(robotiqHand.isConnected())
                  robotiqHand.sendFingersCommand(state);
               break;
            case CLOSE_THUMB:
            case OPEN_THUMB:
               if(robotiqHand.isConnected())
                  robotiqHand.sendThumbCommand(state);
               break;
            case RESET:
               if(robotiqHand.isConnected())
                  robotiqHand.reset();
               break;
            case CONNECT:
               robotiqHand.reconnect();
               break;
            case HOOK:
               //TODO
               break;
            case HALF_CLOSE:
               //TODO
               break;
            default:
               break;
            }
         }
         
         if (manualHandControlProvider.isNewPacketAvailable())
         {
            //TODO
         }
         
         ThreadTools.sleep(10);
      }
   }
   
   public static void main(String[] args)
   {
      CloseableAndDisposableRegistry closeableAndDisposableRegistry = new CloseableAndDisposableRegistry();
      JSAP jsap = new JSAP();
      
      FlaggedOption robotSide = new FlaggedOption("robotSide").setRequired(true).setLongFlag("robotSide").setShortFlag('r').setStringParser(JSAP.STRING_PARSER);
      
      try
      {
         jsap.registerParameter(robotSide);
         JSAPResult config = jsap.parse(args);
         
         if(config.success())
         {
            RobotiqControlThrizzead controlThread = new RobotiqControlThrizzead(RobotSide.valueOf(config.getString("robotSide").toUpperCase()), closeableAndDisposableRegistry);
            controlThread.connect();
            controlThread.run();
         }
      }
      catch (JSAPException e)
      {
         e.printStackTrace();
      }
   }
}