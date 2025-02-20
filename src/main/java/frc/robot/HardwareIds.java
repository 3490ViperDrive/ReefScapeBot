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
        public static final int FRONT_LEFT_DRIVE_MOTOR = 0;
        public static final int FRONT_LEFT_STEER_MOTOR = 4;
        public static final int FRONT_LEFT_ENCODER = 8;

        public static final int FRONT_RIGHT_DRIVE_MOTOR = 1;
        public static final int FRONT_RIGHT_STEER_MOTOR = 5;
        public static final int FRONT_RIGHT_ENCODER = 9;

        public static final int BACK_RIGHT_DRIVE_MOTOR = 2;
        public static final int BACK_RIGHT_STEER_MOTOR = 6;
        public static final int BACK_RIGHT_ENCODER = 10;

        public static final int BACK_LEFT_DRIVE_MOTOR = 3;
        public static final int BACK_LEFT_STEER_MOTOR = 7;
        public static final int BACK_LEFT_ENCODER = 11;

        public static final int IMU = 13; //gyro

        //absolute encoder plugged directly into srx
        public static final int CORAL_LEFT_INTAKE_MOTOR = 12; //sparkmax + neo550
        public static final int CORAL_RIGHT_INTAKE_MOTOR = 13;
        public static final int CORAL_PIVOT_MOTOR = 14; //talon srx + snowblower motor aka windshield wiper motor
        
    }

    public static class I2C {
        public static final int CORAL_COLOR_SENSOR = 0; //oh no
    }
}
