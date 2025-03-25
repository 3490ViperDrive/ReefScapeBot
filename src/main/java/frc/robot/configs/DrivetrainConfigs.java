package frc.robot.configs;

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

public class DrivetrainConfigs {
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
}
