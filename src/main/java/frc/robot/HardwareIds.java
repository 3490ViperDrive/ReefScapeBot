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
        public static final int FRONT_LEFT_DRIVE_MOTOR = 1;
        public static final int FRONT_LEFT_STEER_MOTOR = 5;
        public static final int FRONT_LEFT_ENCODER = 9;

        public static final int FRONT_RIGHT_DRIVE_MOTOR = 2;
        public static final int FRONT_RIGHT_STEER_MOTOR = 6;
        public static final int FRONT_RIGHT_ENCODER = 10;

        public static final int BACK_RIGHT_DRIVE_MOTOR = 3;
        public static final int BACK_RIGHT_STEER_MOTOR = 7;
        public static final int BACK_RIGHT_ENCODER = 11;

        public static final int BACK_LEFT_DRIVE_MOTOR = 4;
        public static final int BACK_LEFT_STEER_MOTOR = 8;
        public static final int BACK_LEFT_ENCODER = 12;

        public static final int IMU = 13; //gyro

        //absolute encoder plugged directly into sparkmax
        public static final int CORAL_LEFT_INTAKE_MOTOR = 12; //sparkmax + neo550
        public static final int CORAL_RIGHT_INTAKE_MOTOR = 13;
        public static final int CORAL_PIVOT_MOTOR = 14; //sparkmax + snowblower motor aka windshield wiper motor
        public static final int CORAL_DISTANCE_SENSOR = 15; //canrange
    }
}
