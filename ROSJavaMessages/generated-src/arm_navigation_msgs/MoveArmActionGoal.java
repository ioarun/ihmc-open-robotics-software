package arm_navigation_msgs;

public interface MoveArmActionGoal extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "arm_navigation_msgs/MoveArmActionGoal";
  static final java.lang.String _DEFINITION = "# ====== DO NOT MODIFY! AUTOGENERATED FROM AN ACTION DEFINITION ======\n\nHeader header\nactionlib_msgs/GoalID goal_id\nMoveArmGoal goal\n";
  std_msgs.Header getHeader();
  void setHeader(std_msgs.Header value);
  actionlib_msgs.GoalID getGoalId();
  void setGoalId(actionlib_msgs.GoalID value);
  arm_navigation_msgs.MoveArmGoal getGoal();
  void setGoal(arm_navigation_msgs.MoveArmGoal value);
}
