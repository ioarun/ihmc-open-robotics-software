package object_manipulation_msgs;

public interface PlaceActionResult extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "object_manipulation_msgs/PlaceActionResult";
  static final java.lang.String _DEFINITION = "# ====== DO NOT MODIFY! AUTOGENERATED FROM AN ACTION DEFINITION ======\n\nHeader header\nactionlib_msgs/GoalStatus status\nPlaceResult result\n";
  std_msgs.Header getHeader();
  void setHeader(std_msgs.Header value);
  actionlib_msgs.GoalStatus getStatus();
  void setStatus(actionlib_msgs.GoalStatus value);
  object_manipulation_msgs.PlaceResult getResult();
  void setResult(object_manipulation_msgs.PlaceResult value);
}
