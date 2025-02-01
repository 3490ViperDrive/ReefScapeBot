package frc.robot.subsystems;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

    //TODO find these on robot
    public static final double FRONT_LEFT_CANCODER_OFFSET = 0;
    public static final double FRONT_RIGHT_CANCODER_OFFSET = 0;
    public static final double BACK_RIGHT_CANCODER_OFFSET = 0;
    public static final double BACK_LEFT_CANCODER_OFFSET = 0;

    //One of the shorter 28" sides are the front of the robot, the two 30" sides are the sides
    //Measured from centers of the wheels (m)
    public static final double TRACK_WIDTH = Units.inchesToMeters(21.367); //robot Y
    public static final double WHEELBASE = Units.inchesToMeters(23.367); //robot X
    public static final double WHEEL_DIAMETER = Units.inchesToMeters(4);

    //L2 gear ratio
    public static final double DRIVE_GEAR_RATIO = 6.75;
    public static final double STEER_COUPLING_RATIO = 3.57; //1 turn of steer falcon shaft : coupling ratio turns of drive motor shaft

    //Reasonable max speeds
    //These should be tested and verified
    public static final double MAX_TRANSLATION_SPEED = 4.78; //m/s (FOC)
    public static final double MAX_ROTATION_SPEED = 1.90; //rot/s

    public Drivetrain() {

    }

    @Override
    public void periodic() {

    }
}
