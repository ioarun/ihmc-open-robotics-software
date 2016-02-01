package pr2_mechanism_msgs;

public interface SwitchControllerActionGoal extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "pr2_mechanism_msgs/SwitchControllerActionGoal";
  static final java.lang.String _DEFINITION = "# ====== DO NOT MODIFY! AUTOGENERATED FROM AN ACTION DEFINITION ======\n\nHeader header\nactionlib_msgs/GoalID goal_id\nSwitchControllerGoal goal\n";
  std_msgs.Header getHeader();
  void setHeader(std_msgs.Header value);
  actionlib_msgs.GoalID getGoalId();
  void setGoalId(actionlib_msgs.GoalID value);
  pr2_mechanism_msgs.SwitchControllerGoal getGoal();
  void setGoal(pr2_mechanism_msgs.SwitchControllerGoal value);
}
