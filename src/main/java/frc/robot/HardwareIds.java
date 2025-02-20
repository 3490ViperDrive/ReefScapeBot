package frc.robot;

/**
 * Constants class to contain strictly hardware IDs or channels needed for motor controllers, encoders, etc.
 * CAN bus IDs, DIO channels, controller ports, etc. should all go here instead of
 * their own separate classes to avoid collisions.
 */
public class HardwareIds {
    /**
     * CAN bus IDs.
     */
    public static class Can {
        //TODO these are reasonable defaults, update to reflect the robot
        public static final int FRONT_LEFT_DRIVE_FALCON = 0;
        public static final int FRONT_LEFT_STEER_FALCON = 4;
        public static final int FRONT_LEFT_CANCODER = 8;

        public static final int FRONT_RIGHT_DRIVE_FALCON = 1;
        public static final int FRONT_RIGHT_STEER_FALCON = 5;
        public static final int FRONT_RIGHT_CANCODER = 9;

        public static final int BACK_RIGHT_DRIVE_FALCON = 2;
        public static final int BACK_RIGHT_STEER_FALCON = 6;
        public static final int BACK_RIGHT_CANCODER = 10;

        public static final int BACK_LEFT_DRIVE_FALCON = 3;
        public static final int BACK_LEFT_STEER_FALCON = 7;
        public static final int BACK_LEFT_CANCODER = 11;

        public static final int PIGEON = 0; //gyro

        public static final int ALGAE_INTAKE_MOTOR1 = 1; //magic number in use
        public static final int ALGAE_INTAKE_MOTOR2 = 2; //magic number in use
        public static final int ALGAE_PIVOT_MOTOR = 3; //magic number in use
        
    }
}
