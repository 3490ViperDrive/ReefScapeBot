package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.SteerFeedbackType;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.SteerMotorArrangement;
import com.ctre.phoenix6.swerve.SwerveModuleConstantsFactory;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModuleConstants;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.ClosedLoopOutputType;
import com.ctre.phoenix6.swerve.SwerveModuleConstants.DriveMotorArrangement;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.units.measure.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.HardwareIds;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

/**
 * The Drivetrain class is responsible for managing all the hardware components of the swerve drive.
 * It leverages the swerve library from CTRE v6.
 */
public class Drivetrain extends SubsystemBase {

    public static Drivetrain instance;

    public static final Angle FRONT_LEFT_CANCODER_OFFSET = Rotations.of(-0.443359);
    public static final Angle FRONT_RIGHT_CANCODER_OFFSET = Rotations.of(-0.159180);
    public static final Angle BACK_RIGHT_CANCODER_OFFSET = Rotations.of(-0.344971);
    public static final Angle BACK_LEFT_CANCODER_OFFSET = Rotations.of(0.138428);

    //28"x32" frame, one of the shorter sides are the front
    //Measured from centers of the wheels
    public static final Distance TRACK_WIDTH = Inches.of(21.367); //robot Y
    public static final Distance WHEELBASE = Inches.of(25.367); //robot X
    //this will change as the colsons wear down
    public static final Distance WHEEL_DIAMETER = Inches.of(4); 
    //L2 gear ratio
    public static final double DRIVE_GEAR_RATIO = 6.746031746031747;
    public static final double STEER_GEAR_RATIO = 12.8;
    //1 turn of steer falcon shaft : coupling ratio turns of drive motor shaft
    public static final double STEER_COUPLING_RATIO = 3.5714285714285716;

    //Reasonable max speeds
    //These should be tested and verified
    public static final LinearVelocity MAX_TRANSLATION_SPEED = MetersPerSecond.of(4.78); //FOC
    public static final AngularVelocity MAX_ROTATION_SPEED = RotationsPerSecond.of(1.90);

    public static final ClosedLoopOutputType DRIVE_CLOSED_LOOP_OUTPUT =
                            ClosedLoopOutputType.Voltage;
    public static final ClosedLoopOutputType STEER_CLOSED_LOOP_OUTPUT =
                            ClosedLoopOutputType.Voltage;

    public static final DriveMotorArrangement DRIVE_MOTOR_TYPE =
                            DriveMotorArrangement.TalonFX_Integrated;
    public static final SteerMotorArrangement STEER_MOTOR_TYPE =
                            SteerMotorArrangement.TalonFX_Integrated;

    public static final boolean DRIVE_MOTOR_INVERTED = false;
    public static final boolean STEER_MOTOR_INVERTED = false;
    public static final boolean ENCODER_INVERTED = false;

    public static final SteerFeedbackType STEER_FEEDBACK_SOURCE = SteerFeedbackType.FusedCANcoder;

    //these are only used for simulation
    public static final MomentOfInertia DRIVE_INERTIA = KilogramSquareMeters.of(0.01);
    public static final MomentOfInertia STEER_INERTIA = KilogramSquareMeters.of(0.01);

    //TODO tune drive gains
    //these gains are pulled from a fresh tuner project (steer kP halved)
    private static final Slot0Configs driveGains = new Slot0Configs()
                                                       .withKP(0.1).withKI(0).withKD(0)
													   .withKS(0).withKV(0.124);
    
    private static final Slot0Configs steerGains = new Slot0Configs()
                                                       .withKP(50).withKI(0).withKD(0.5)
													   .withKS(0.1).withKV(1.59).withKA(0)
													   .withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);

    private final SwerveDrivetrain<TalonFX, TalonFX, CANcoder> swerve;
    
    private SwerveDrivetrain.SwerveDriveState currentState;

    private SwerveRequest.ApplyRobotSpeeds pathPlannerRequest;

    /**
     * Creates a new Drivetrain.
     */
    public Drivetrain() {
        instance = this;
         //yoinked and modified from Crescendobot
        pathPlannerRequest = new SwerveRequest.ApplyRobotSpeeds().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
        final SwerveDrivetrainConstants drivetrainConstants = new SwerveDrivetrainConstants();
        final SwerveModuleConstantsFactory<TalonFXConfiguration,
                                           TalonFXConfiguration,
                                           CANcoderConfiguration> commonModuleConstants =
                                               new SwerveModuleConstantsFactory<>();
        drivetrainConstants
            .withPigeon2Id(HardwareIds.Can.IMU);
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
                        HardwareIds.Can.FRONT_LEFT_STEER_MOTOR,
                        HardwareIds.Can.FRONT_LEFT_DRIVE_MOTOR,
                        HardwareIds.Can.FRONT_LEFT_ENCODER, FRONT_LEFT_CANCODER_OFFSET,
                        WHEELBASE.div(2), TRACK_WIDTH.div(2),
                        DRIVE_MOTOR_INVERTED, STEER_MOTOR_INVERTED, ENCODER_INVERTED);
        frontRight = commonModuleConstants.createModuleConstants(
                        HardwareIds.Can.FRONT_RIGHT_STEER_MOTOR,
                        HardwareIds.Can.FRONT_RIGHT_DRIVE_MOTOR,
                        HardwareIds.Can.FRONT_RIGHT_ENCODER, FRONT_RIGHT_CANCODER_OFFSET,
                        WHEELBASE.div(2), TRACK_WIDTH.div(2).unaryMinus(),
                        DRIVE_MOTOR_INVERTED, STEER_MOTOR_INVERTED, ENCODER_INVERTED);
        backRight = commonModuleConstants.createModuleConstants(
                        HardwareIds.Can.BACK_RIGHT_STEER_MOTOR,
                        HardwareIds.Can.BACK_RIGHT_DRIVE_MOTOR,
                        HardwareIds.Can.BACK_RIGHT_ENCODER, BACK_RIGHT_CANCODER_OFFSET,
                        WHEELBASE.div(2).unaryMinus(), TRACK_WIDTH.div(2).unaryMinus(),
                        DRIVE_MOTOR_INVERTED, STEER_MOTOR_INVERTED, ENCODER_INVERTED);
        backLeft = commonModuleConstants.createModuleConstants(
                        HardwareIds.Can.BACK_LEFT_STEER_MOTOR,
                        HardwareIds.Can.BACK_LEFT_DRIVE_MOTOR,
                        HardwareIds.Can.BACK_LEFT_ENCODER, BACK_LEFT_CANCODER_OFFSET,
                        WHEELBASE.div(2).unaryMinus(), TRACK_WIDTH.div(2),
                        DRIVE_MOTOR_INVERTED, STEER_MOTOR_INVERTED, ENCODER_INVERTED);

        swerve = new SwerveDrivetrain<TalonFX, TalonFX, CANcoder>(
            TalonFX::new, TalonFX::new, CANcoder::new,
            drivetrainConstants,
            frontLeft, frontRight, backRight, backLeft
        );

       

        RobotConfig config;

            try{
                config = RobotConfig.fromGUISettings();
                AutoBuilder.configure(
                this::getPose, // Robot pose supplier
                this::resetPose, // Method to reset odometry (will be called if your auto has a starting pose)
                this::getChassisSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                (speeds, feedforwards) -> {
                    swerve.setControl(pathPlannerRequest.withSpeeds(speeds));
                }, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
                new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
                        new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                        new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
                ),

                config, // The robot configuration
                () -> {
                // Boolean supplier that controls when the path will be mirrored for the red alliance
                // This will flip the path being followed to the red side of the field.
                // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

                var alliance = DriverStation.getAlliance();
                if (alliance.isPresent()) {
                    return alliance.get() == DriverStation.Alliance.Red;
                }
                return false;
                },
                this // Reference to this subsystem to set requirements
        );
            } catch (Exception e) {
                System.out.println("HUMANITY WAS A MISTAKE");
                e.printStackTrace();
            }

            
    } //Drivetrain constructor

    @Override
    public void periodic() {
        currentState = swerve.getState();
        SmartDashboard.putData("Drivetrain", this);
    }

    //Call this in the execute method of drive cmds
    public void applySwerveRequest(SwerveRequest request) {
        swerve.setControl(request);
    }

    //This should not be called during an actual match!
    //Auto routines should properly initialize the robot's position.
    public void zeroYaw() {
        swerve.seedFieldCentric();
    }

    @Logged
    public ChassisSpeeds getChassisSpeeds() {
        return currentState.Speeds;
    }

    @Logged
    public SwerveModuleState[] getModuleStates() {
        return currentState.ModuleStates;
    }

    @Logged
    public SwerveModuleState[] getModuleTargets() {
        return currentState.ModuleTargets;
    }

    @Logged
    public Pose2d getPose() {
        return currentState.Pose;
    }

    // public Command resetPoseCommand(Pose2d pose) {
    //     return runOnce(() -> swerve.resetPose(pose));
    // }

    public void resetPose(Pose2d jojos){
        swerve.resetPose(jojos);
    }
}
