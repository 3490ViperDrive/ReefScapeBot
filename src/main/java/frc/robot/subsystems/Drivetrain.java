package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.SteerFeedbackType;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.SteerMotorArrangement;
import com.ctre.phoenix6.swerve.SwerveModuleConstantsFactory;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.ClosedLoopOutputType;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.DriveMotorArrangement;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.units.measure.*;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.HardwareIds;

/**
 * The Drivetrain class is responsible for managing all the hardware components of the swerve drive.
 * It leverages the swerve library from CTRE v6.
 */
public class Drivetrain extends SubsystemBase {

    //TODO find these on robot
    public static final Angle FRONT_LEFT_CANCODER_OFFSET = Degrees.of(0);
    public static final Angle FRONT_RIGHT_CANCODER_OFFSET = Degrees.of(0);
    public static final Angle BACK_RIGHT_CANCODER_OFFSET = Degrees.of(0);
    public static final Angle BACK_LEFT_CANCODER_OFFSET = Degrees.of(0);

    //28"x32" frame, one of the shorter sides are the front
    //Measured from centers of the wheels
    public static final Distance TRACK_WIDTH = Inches.of(21.367); //robot Y
    public static final Distance WHEELBASE = Inches.of(25.367); //robot X
    //this will change as the colsons wear down
    public static final Distance WHEEL_DIAMETER = Inches.of(4); 
    //L2 gear ratio
    public static final double DRIVE_GEAR_RATIO = 6.75;
    public static final double STEER_GEAR_RATIO = 12.8;
    //1 turn of steer falcon shaft : coupling ratio turns of drive motor shaft
    public static final double STEER_COUPLING_RATIO = 3.57;

    //Reasonable max speeds
    //These should be tested and verified
    public static final LinearVelocity MAX_TRANSLATION_SPEED = MetersPerSecond.of(4.78); //FOC
    public static final AngularVelocity MAX_ROTATION_SPEED = RotationsPerSecond.of(1.90);

    public static final ClosedLoopOutputType DRIVE_CLOSED_LOOP_OUTPUT =
                            ClosedLoopOutputType.TorqueCurrentFOC;
    public static final ClosedLoopOutputType STEER_CLOSED_LOOP_OUTPUT =
                            ClosedLoopOutputType.Voltage;

    public static final DriveMotorArrangement DRIVE_MOTOR_TYPE =
                            DriveMotorArrangement.TalonFX_Integrated;
    public static final SteerMotorArrangement STEER_MOTOR_TYPE =
                            SteerMotorArrangement.TalonFX_Integrated;

    public static final boolean DRIVE_MOTOR_INVERTED = false; //double check these ones
    public static final boolean STEER_MOTOR_INVERTED = true;
    public static final boolean ENCODER_INVERTED = false;

    public static final SteerFeedbackType STEER_FEEDBACK_SOURCE = SteerFeedbackType.FusedCANcoder;

    //these are only used for simulation
    public static final MomentOfInertia DRIVE_INERTIA = KilogramSquareMeters.of(0.01);
    public static final MomentOfInertia STEER_INERTIA = KilogramSquareMeters.of(0.01);

    //TODO tune gains
    //these gains worked on the last robot (?) but they will need adjustment for this robot, probably
    private static final Slot0Configs driveGains = new Slot0Configs()
                                                       .withKP(3).withKI(0).withKD(0)
                                                       .withKS(0).withKV(0).withKA(0);
    
    private static final Slot0Configs steerGains = new Slot0Configs()
                                                       .withKP(50).withKI(0).withKD(0.2)
                                                       .withKS(0).withKV(1.5).withKA(0);

    private final SwerveDrivetrain<TalonFX, TalonFX, CANcoder> swerve;

    /**
     * Creates a new Drivetrain.
     */
    public Drivetrain() {
        final SwerveDrivetrainConstants drivetrainConstants = new SwerveDrivetrainConstants();
        final SwerveModuleConstantsFactory<TalonFXConfiguration,
                                           TalonFXConfiguration,
                                           CANcoderConfiguration> commonModuleConstants =
                                               new SwerveModuleConstantsFactory<>();
        drivetrainConstants
            .withPigeon2Id(HardwareIds.Can.PIGEON);
            //add pigeon configuration here if necessary
            //withPigeon2Configs()
        commonModuleConstants
            .withDriveMotorGearRatio(DRIVE_GEAR_RATIO)
            .withSteerMotorGearRatio(STEER_GEAR_RATIO)
            .withCouplingGearRatio(STEER_COUPLING_RATIO)
            .withWheelRadius(WHEEL_DIAMETER.div(2))
            .withSpeedAt12Volts(MAX_TRANSLATION_SPEED)
            .withDriveMotorClosedLoopOutput(DRIVE_CLOSED_LOOP_OUTPUT)
            .withSteerMotorClosedLoopOutput(STEER_CLOSED_LOOP_OUTPUT)
            .withFeedbackSource(STEER_FEEDBACK_SOURCE)
            .withDriveMotorType(DRIVE_MOTOR_TYPE)
            .withSteerMotorType(STEER_MOTOR_TYPE)
            .withDriveInertia(DRIVE_INERTIA)
            .withSteerInertia(STEER_INERTIA)
            .withDriveMotorGains(driveGains)
            .withSteerMotorGains(steerGains);
            //add drive/steer falcon or cancoder configuration here if necessary
            //withDriveMotorInitialConfigs() withSteerMotorInitialConfigs() withEncoderInitialConfigs()
            
        final SwerveModuleConstants<TalonFXConfiguration,
                                    TalonFXConfiguration,
                                    CANcoderConfiguration> frontLeft, frontRight, backRight, backLeft;
        frontLeft = commonModuleConstants.createModuleConstants(
                        HardwareIds.Can.FRONT_LEFT_STEER_FALCON,
                        HardwareIds.Can.FRONT_LEFT_DRIVE_FALCON,
                        HardwareIds.Can.FRONT_LEFT_CANCODER, FRONT_LEFT_CANCODER_OFFSET,
                        WHEELBASE.div(2), TRACK_WIDTH.div(2),
                        DRIVE_MOTOR_INVERTED, STEER_MOTOR_INVERTED, ENCODER_INVERTED);
        frontRight = commonModuleConstants.createModuleConstants(
                        HardwareIds.Can.FRONT_RIGHT_STEER_FALCON,
                        HardwareIds.Can.FRONT_RIGHT_DRIVE_FALCON,
                        HardwareIds.Can.FRONT_RIGHT_CANCODER, FRONT_RIGHT_CANCODER_OFFSET,
                        WHEELBASE.div(2), TRACK_WIDTH.div(2).unaryMinus(),
                        DRIVE_MOTOR_INVERTED, STEER_MOTOR_INVERTED, ENCODER_INVERTED);
        backRight = commonModuleConstants.createModuleConstants(
                        HardwareIds.Can.BACK_RIGHT_STEER_FALCON,
                        HardwareIds.Can.BACK_RIGHT_DRIVE_FALCON,
                        HardwareIds.Can.BACK_RIGHT_CANCODER, BACK_RIGHT_CANCODER_OFFSET,
                        WHEELBASE.div(2).unaryMinus(), TRACK_WIDTH.div(2).unaryMinus(),
                        DRIVE_MOTOR_INVERTED, STEER_MOTOR_INVERTED, ENCODER_INVERTED);
        backLeft = commonModuleConstants.createModuleConstants(
                        HardwareIds.Can.BACK_LEFT_STEER_FALCON,
                        HardwareIds.Can.BACK_LEFT_DRIVE_FALCON,
                        HardwareIds.Can.BACK_LEFT_CANCODER, BACK_LEFT_CANCODER_OFFSET,
                        WHEELBASE.div(2).unaryMinus(), TRACK_WIDTH.div(2),
                        DRIVE_MOTOR_INVERTED, STEER_MOTOR_INVERTED, ENCODER_INVERTED);

        swerve = new SwerveDrivetrain<TalonFX, TalonFX, CANcoder>(
            TalonFX::new, TalonFX::new, CANcoder::new,
            drivetrainConstants,
            frontLeft, frontRight, backRight, backLeft
        );
    }

    @Override
    public void periodic() {}

    //Call this in the execute method of drive cmds
    public void applySwerveRequest(SwerveRequest request) {
        swerve.setControl(request);
    }

    //This should not be called during an actual match!
    //Auto routines should properly initialize the robot's position.
    public void zeroYaw() {
        swerve.seedFieldCentric();
    }
}
