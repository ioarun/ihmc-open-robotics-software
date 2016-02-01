package move_arm_head_monitor;

public interface HeadMonitorActionFeedback extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "move_arm_head_monitor/HeadMonitorActionFeedback";
  static final java.lang.String _DEFINITION = "# ====== DO NOT MODIFY! AUTOGENERATED FROM AN ACTION DEFINITION ======\n\nHeader header\nactionlib_msgs/GoalStatus status\nHeadMonitorFeedback feedback\n";
  std_msgs.Header getHeader();
  void setHeader(std_msgs.Header value);
  actionlib_msgs.GoalStatus getStatus();
  void setStatus(actionlib_msgs.GoalStatus value);
  move_arm_head_monitor.HeadMonitorFeedback getFeedback();
  void setFeedback(move_arm_head_monitor.HeadMonitorFeedback value);
}
