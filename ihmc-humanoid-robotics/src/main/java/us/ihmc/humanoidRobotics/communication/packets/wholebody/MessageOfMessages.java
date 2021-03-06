package us.ihmc.humanoidRobotics.communication.packets.wholebody;

import java.util.ArrayList;
import java.util.List;

import us.ihmc.communication.packets.MultiplePacketHolder;
import us.ihmc.communication.packets.Packet;
import us.ihmc.communication.packets.VisualizablePacket;

/**
 *  MessageOfMessages provides a generic way to send a collection of messages to the controller.
 */
public class MessageOfMessages extends Packet<MessageOfMessages> implements VisualizablePacket, MultiplePacketHolder
{
   
   private ArrayList<Packet<?>> packets = new ArrayList<>();
   
   public MessageOfMessages()
   {
      setUniqueId(VALID_MESSAGE_DEFAULT_ID);
   }
   
   public MessageOfMessages(ArrayList<Packet<?>> messages)
   {
      setUniqueId(VALID_MESSAGE_DEFAULT_ID);
      packets.clear();
      packets.addAll(messages);
   }

   public MessageOfMessages(Packet<?>... messages)
   {
      setUniqueId(VALID_MESSAGE_DEFAULT_ID);
      packets.clear();
      for(Packet<?> packet : messages)
      {
         packets.add(packet);
      }
   }
   
   public void addPacket(Packet<?>... messages)
   {
      for(Packet<?> packet : messages)
      {
         packets.add(packet);
      }
   }

   public void clear()
   {
      packets.clear();
   }

   @Override
   public List<Packet<?>> getPackets()
   {
      return packets;
   }

   @SuppressWarnings({"unchecked", "rawtypes"})
   @Override
   public boolean epsilonEquals(MessageOfMessages other, double epsilon)
   {
      if(packets.size() != other.packets.size())
      {
         return false;
      }
      
      for(int i = 0; i < packets.size(); i++)
      {
         Packet packet = packets.get(i);
         Packet otherPacket = other.packets.get(i);
         packet.epsilonEquals(otherPacket, epsilon);
      }
      
      return true;
   }
}
