package ihmc_msgs;

public interface EuclideanTrajectoryPointRosMessage extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "ihmc_msgs/EuclideanTrajectoryPointRosMessage";
  static final java.lang.String _DEFINITION = "## EuclideanTrajectoryPointRosMessage\n# This class is used to build trajectory messages in taskspace. It holds the only the translational information for one trajectory point (position & linear velocity). Feel free to look at SO3TrajectoryPointMessage (rotational) and SE3TrajectoryPointMessage (rotational AND translational)\n\n# Time at which the trajectory point has to be reached. The time is relative to when the trajectory starts.\nfloat64 time\n\n# Define the desired 3D position to be reached at this trajectory point. It is expressed in world frame.\ngeometry_msgs/Vector3 position\n\n# Define the desired 3D linear velocity to be reached at this trajectory point. It is expressed in world frame.\ngeometry_msgs/Vector3 linear_velocity\n\n# A unique id for the current message. This can be a timestamp or sequence number.\n# Only the unique id in the top level message is used, the unique id in nested messages is ignored.\n# Use /output/last_received_message for feedback about when the last message was received.\n# A message with a unique id equals to 0 will be interpreted as invalid and will not be processed by the controller.\nint64 unique_id\n\n\n";
  double getTime();
  void setTime(double value);
  geometry_msgs.Vector3 getPosition();
  void setPosition(geometry_msgs.Vector3 value);
  geometry_msgs.Vector3 getLinearVelocity();
  void setLinearVelocity(geometry_msgs.Vector3 value);
  long getUniqueId();
  void setUniqueId(long value);
}
