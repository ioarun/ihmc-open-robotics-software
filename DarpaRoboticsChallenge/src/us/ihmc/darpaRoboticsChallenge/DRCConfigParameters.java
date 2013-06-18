package us.ihmc.darpaRoboticsChallenge;

import us.ihmc.darpaRoboticsChallenge.configuration.DRCLocalCloudConfig;
import us.ihmc.darpaRoboticsChallenge.configuration.DRCLocalCloudConfig.LocalCloudMachines;
import us.ihmc.darpaRoboticsChallenge.drcRobot.DRCRobotParameters;

public class DRCConfigParameters
{
   public static final boolean USE_DUMMY_DRIVNG = false;
   public static final boolean RESTART_FOR_FANCY_CONTROL = true;     // Enable for testing standup
   
   public static final boolean ALLOW_LAG_SIMULATION = true;
   public static final boolean ENABLE_LAG_SIMULATION_ON_START =  false;

   static
   {
      if (ALLOW_LAG_SIMULATION)
      {
         System.err.println("Warning: Allowing simulation of lag");
      }
   }

   public static final boolean USE_COLLISIONS_MESHS_FOR_VISUALIZATION = false;
   public static final boolean SEND_HIGH_SPEED_CONFIGURATION_DATA = false;

   // TODO: Temporary static variable for testing getting into the car
   public static final boolean TEST_GETTING_INTO_CAR = false;    // true;

   public static final boolean USE_SLIDER_FOR_POSE_PLAYBACK = false;    // true;

   public static final boolean USE_SUPER_DUPER_HIGH_RESOLUTION_FOR_COMMS = false;

   public static final boolean USE_GAZEBO_PHYSICS = false;    // TODO: This one is

   // needed just for
   // FlatGroundWalkingTrack
   // in Gazebo...

   public static final boolean USE_PERFECT_SENSORS = false;

   static
   {
      if (USE_PERFECT_SENSORS)
         System.err.println("Warning! Using Perfect Sensors!");
   }

   public static final String[] JOINTS_TO_IGNORE_FOR_GAZEBO = {"hokuyo_joint"};
   public static final boolean SHOW_SCS_GUI_FOR_GAZEBO = true;

   public static final boolean SHOW_BANDWIDTH_DIALOG = false;

   public static final double ESTIMATE_DT = 0.001;

   // Set whether or not to use GFE Robot Model
   public static final boolean USE_GFE_ROBOT_MODEL = true;

   // Convenience field
   public static final boolean USE_R2_ROBOT_MODEL = !USE_GFE_ROBOT_MODEL;

   // Networking
   public static final String LOCALHOST = "localhost";
   public static final String CLOUD_MINION1_IP = DRCLocalCloudConfig.getIPAddress(LocalCloudMachines.CLOUDMINION_1);
   public static final String CLOUD_MINION2_IP = DRCLocalCloudConfig.getIPAddress(LocalCloudMachines.CLOUDMINION_2);
   public static final String CLOUD_MINION3_IP = DRCLocalCloudConfig.getIPAddress(LocalCloudMachines.CLOUDMINION_3);
   public static final String CLOUD_MINION4_IP = DRCLocalCloudConfig.getIPAddress(LocalCloudMachines.CLOUDMINION_4);
   public static final String CLOUD_MINION5_IP = DRCLocalCloudConfig.getIPAddress(LocalCloudMachines.CLOUDMINION_5);
   public static final String CLOUD_MINION6_IP = DRCLocalCloudConfig.getIPAddress(LocalCloudMachines.CLOUDMINION_6);
   public static final String CLOUD_MONSTER_JR_IP = DRCLocalCloudConfig.getIPAddress(LocalCloudMachines.CLOUDMONSTER_JR);
   public static final String CLOUD_MONSTER_IP = DRCLocalCloudConfig.getIPAddress(LocalCloudMachines.CLOUDMONSTER);

   public static final String CONSTELLATION_SIMULATOR_COMPUTER_VPN_IP = "10.0.0.51";
   public static final String CONSTELLATION_FIELD_COMPUTER_1_VPN_IP = "10.0.0.52";
   public static final String CONSTELLATION_FIELD_COMPUTER_2_VPN_IP = "10.0.0.53";

   public static final String GAZEBO_HOST = LOCALHOST; //CONSTELLATION_SIMULATOR_COMPUTER_VPN_IP; //CONSTELLATION_SIMULATOR_COMPUTER_VPN_IP;
   public static final String SCS_MACHINE_IP_ADDRESS = LOCALHOST; //CONSTELLATION_FIELD_COMPUTER_2_VPN_IP; //CONSTELLATION_FIELD_COMPUTER_2_VPN_IP;    // CLOUD_MONSTER_IP;
   public static final String NET_PROC_MACHINE_IP_ADDRESS = LOCALHOST; //CONSTELLATION_FIELD_COMPUTER_1_VPN_IP; //CONSTELLATION_FIELD_COMPUTER_1_VPN_IP;    // SCS_MACHINE_IP_ADDRESS;

   public static final String OPERATOR_INTERFACE_IP_ADDRESS = LOCALHOST;

   public static final String ROS_MASTER_URI = "http://" + GAZEBO_HOST + ":11311";
   public static final int CONTROLLER_TO_UI_TCP_PORT = 4893;

   public static final int NETWORK_PROCESSOR_TO_CONTROLLER_TCP_PORT = 4895;

   public static final int NETWORK_PROCESSOR_TO_UI_TCP_PORT = 4897;

   public static final int NETWORK_PROCESSOR_TO_UI_RAW_PROTOCOL_TCP_PORT = 4898;

   public static final int NETWORK_PROCESSOR_CLOUD_DISPATCHER_BACKEND_TCP_PORT = 5000;

   public static final int CONTROLLER_CLOUD_DISPATCHER_BACKEND_TCP_PORT = 5002;

   public static final long ROBOT_JOINT_SERVER_UPDATE_MILLIS = 100;

   // Video Settings
   public static final boolean STREAM_VIDEO = true;

   // State Estimator
   public static final boolean USE_STATE_ESTIMATOR = true;
   public static final boolean INTRODUCE_FILTERED_GAUSSIAN_POSITIONING_ERROR = false;
   public static final double NOISE_FILTER_ALPHA = 1e-1;
   public static final double POSITION_NOISE_STD = 0.01;
   public static final double QUATERNION_NOISE_STD = 0.01;

   // LIDAR:
   public static final double LIDAR_SPINDLE_VELOCITY = 2.5;

   public static final boolean STREAM_POLAR_LIDAR = true;
   public static final int LIDAR_UPDATE_RATE_OVERRIDE = 5;
   public static final int LIDAR_SWEEPS_PER_SCAN = 1;
   public static final int LIDAR_POINTS_PER_SWEEP = 640; //I assume this number will never be 720 in SCS -Gray.
   public static final boolean OVERRIDE_DRC_LIDAR_CONFIG = true;
   public static final float LIDAR_MIN_DISTANCE = 0.2f;
   public static final float LIDAR_MAX_DISTANCE = 10.0f;
   public static final float LIDAR_NEAR_SCAN_MAX_DISTANCE = 3.0f;

   public static final float LIDAR_SWEEP_MAX_YAW = 0.8f;
   public static final float LIDAR_SWEEP_MIN_YAW = -0.8f;
   public static final float LIDAR_SCAN_MAX_ROLL = 0.0f;    // rolls the LIDAR to

   // simulate a faster
   // update rate.
   public static final float LIDAR_SCAN_MIN_ROLL = 0.0f;
   public static final float LIDAR_ANGLE_INCREMENT = (float) Math.toRadians(0.25);
   public static final float LIDAR_TIME_INCREMENT = 0.0f;
   public static final float LIDAR_SCAN_TIME = 0.0f;
   public static final double LIDAR_NOISE_LEVEL_OVERRIDE = 0.005;    // DRCGazebo

   // will
   // simulate
   // with:
   // 0.005
   public static final boolean DEBUG_GAZEBO_LIDAR = false;

   // LIDAR Processor
   public static final boolean LIDAR_PROCESSOR_TIMING_REPORTING_ON = false;
   public static final double GRID_RESOLUTION = 0.025;    // in meters
   public static final double OCTREE_RESOLUTION_WHEN_NOT_USING_RESOLUTION_SPHERE = 0.05;
   public static final float QUADTREE_HEIGHT_THRESHOLD = 0.05f;
   public static final double LIDAR_BLINDNESS_CYLINDAR_SQUARED_RADIUS = 0.1;
   public static final boolean HIDE_THINGS_ABOVE_HEAD_FROM_LIDAR = true;

   // Footstep Generator
   public static final double BOUNDING_BOX_FOR_FOOTSTEP_HEIGHT_FINDING_SIDE_LENGTH =
      (1 + 0.3) * 2
      * Math.sqrt(DRCRobotParameters.DRC_ROBOT_FOOT_FORWARD * DRCRobotParameters.DRC_ROBOT_FOOT_FORWARD
                  + 0.25 * DRCRobotParameters.DRC_ROBOT_FOOT_WIDTH * DRCRobotParameters.DRC_ROBOT_FOOT_WIDTH);

   public static final int JOINT_CONFIGURATION_RATE_IN_MS = 10;

   // Resolution Sphere
   public static final boolean USE_RESOLUTION_SPHERE = true;
   public static final double LIDAR_RESOLUTION_SPHERE_OUTER_RESOLUTION = 0.5;
   public static final double LIDAR_RESOLUTION_SPHERE_OUTER_RADIUS = 6.0;
   public static final double LIDAR_RESOLUTION_SPHERE_INNER_RESOLUTION = 0.02;
   public static final double LIDAR_RESOLUTION_SPHERE_INNER_RADIUS = 1.0;   
   public static final double LIDAR_RESOLUTION_SPHERE_DISTANCE_FROM_HEAD = 1.0;

   public static final boolean USE_TABS_IN_UI = true;

   // Hand Controller
   public static final boolean USE_PURE_POSITION_CONTROL_FOR_HANDS = false;

   public static final int CHEATING_POLARIS_PORT = 1543;
   public static final String CHEATING_POLARIS_HOST = LOCALHOST;

   
}
