package object_manipulation_msgs;

public interface ReactiveGraspFeedback extends org.ros.internal.message.Message {
  static final java.lang.String _TYPE = "object_manipulation_msgs/ReactiveGraspFeedback";
  static final java.lang.String _DEFINITION = "# ====== DO NOT MODIFY! AUTOGENERATED FROM AN ACTION DEFINITION ======\n\n# which phase the grasp is in\n\nManipulationPhase manipulation_phase\n\n\n\n";
  object_manipulation_msgs.ManipulationPhase getManipulationPhase();
  void setManipulationPhase(object_manipulation_msgs.ManipulationPhase value);
}
