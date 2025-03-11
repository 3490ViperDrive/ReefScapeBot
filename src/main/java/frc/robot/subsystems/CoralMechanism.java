package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.ProximityParamsConfigs;
import com.ctre.phoenix6.configs.ToFParamsConfigs;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.signals.UpdateModeValue;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.HardwareIds;
import frc.robot.Robot;
import frc.robot.utils.SparkMaxConfigUtil;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.epilogue.Logged.Importance;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CoralMechanism extends SubsystemBase {
    
    @Logged
    private final SparkMax leftIntakeMotor;
    @Logged
    private final SparkMax rightIntakeMotor;
    @Logged
    private final SparkMax pivotMotor;
    private final SparkClosedLoopController pivotClosedLoopController;
    private final CANrange distanceSensor;

    private final AbsoluteEncoder pivotEncoder;
    private final StatusSignal<Distance> distanceSensorDistanceSignal;
    private final StatusSignal<Boolean> distanceSensorIsDetectedSignal;

    @Logged
    private double currentPivotSetpoint;
    @Logged
    private boolean pivotClosedLoopModeActive;

    public static final boolean INTAKE_INVERT_LEFT = true;
    public static final boolean INTAKE_INVERT_RIGHT = false;
    public static final boolean INTAKE_INVERT_FOLLOWER = true; //right

    //these are guessed
    public static final Current INTAKE_CURRENT_LIMIT_FREE = Amps.of(40);
    public static final Current INTAKE_CURRENT_LIMIT_STALL = Amps.of(20);

    public static final IdleMode INTAKE_IDLE_MODE = IdleMode.kBrake;

    public static final boolean PIVOT_INVERT = true;

    //these are also guessed
    public static final Current PIVOT_CURRENT_LIMIT_FREE = Amps.of(50);
    public static final Current PIVOT_CURRENT_LIMIT_STALL = Amps.of(30);

    public static final IdleMode PIVOT_IDLE_MODE = IdleMode.kBrake;

    //TODO tune
    public static class PivotClosedLoopGains {
        public static final double P = 55; //volts per rotation of error
        public static final double D = 0; //volts per rotation of error per second
        public static final double G = 1.5;
        public static final double S = 0.5;
    }

    
    public static final FeedbackSensor PIVOT_FEEDBACK_SENSOR = FeedbackSensor.kAbsoluteEncoder;

    public static final Angle PIVOT_ENCODER_ZERO_OFFSET = Rotations.of(0.27);
    public static final boolean PIVOT_ENCODER_ZERO_CENTERED = true;
    public static final boolean PIVOT_ENCODER_INVERT = false;

    public static final UpdateModeValue DISTANCE_SENSOR_UPDATE_MODE = UpdateModeValue.ShortRange100Hz;
    public static final double DISTANCE_SENSOR_SIGNAL_STRENGTH_THRESHOLD = 2700;
    public static final double DISTANCE_SENSOR_PROXIMITY_THRESHOLD = 0.1;

    public static final double PIVOT_AT_SETPOINT_TOLERANCE = 0.02; //1/50th of a rotation in either direction

    //TODO TUNE HIGHLY IMPORTANT
    public static final double STRAIGHT_ANGLE = 0.0;

    public CoralMechanism() {
        //For backup coral pivot motor
        //pivotMotor = new SparkMax(HardwareIds.Can.CORAL_PIVOT_MOTOR, MotorType.kBrushless);
        pivotMotor = new SparkMax(HardwareIds.Can.CORAL_PIVOT_MOTOR, MotorType.kBrushed);
        leftIntakeMotor = new SparkMax(HardwareIds.Can.CORAL_LEFT_INTAKE_MOTOR, MotorType.kBrushless);
        rightIntakeMotor = new SparkMax(HardwareIds.Can.CORAL_RIGHT_INTAKE_MOTOR, MotorType.kBrushless);
        distanceSensor = new CANrange(HardwareIds.Can.CORAL_DISTANCE_SENSOR);
        currentPivotSetpoint = 0;
        pivotClosedLoopModeActive = false;

        //make motor configurations
        final SparkMaxConfig leftIntakeMotorConfiguration = new SparkMaxConfig();
        final SparkMaxConfig rightIntakeMotorConfiguration = new SparkMaxConfig();
        final ClosedLoopConfig pivotClosedLoopConfiguration = new ClosedLoopConfig();
        final AbsoluteEncoderConfig pivotEncoderConfiguration = new AbsoluteEncoderConfig();
        final SparkMaxConfig pivotMotorConfiguration = new SparkMaxConfig();
        final CANrangeConfiguration distanceSensorConfiguration = new CANrangeConfiguration();
        leftIntakeMotorConfiguration
            .inverted(INTAKE_INVERT_LEFT)
            .smartCurrentLimit((int) INTAKE_CURRENT_LIMIT_STALL.magnitude(),
                               (int) INTAKE_CURRENT_LIMIT_FREE.magnitude())
            .idleMode(INTAKE_IDLE_MODE);
        rightIntakeMotorConfiguration
            .apply(leftIntakeMotorConfiguration)
            .inverted(INTAKE_INVERT_RIGHT)
            .follow(leftIntakeMotor, INTAKE_INVERT_FOLLOWER); 
        pivotClosedLoopConfiguration
            .p(PivotClosedLoopGains.P)
            .d(PivotClosedLoopGains.D)
            .feedbackSensor(PIVOT_FEEDBACK_SENSOR);
        pivotEncoderConfiguration
            .inverted(PIVOT_ENCODER_INVERT)
            .zeroOffset(PIVOT_ENCODER_ZERO_OFFSET.in(Rotations))
            .zeroCentered(PIVOT_ENCODER_ZERO_CENTERED);
        pivotMotorConfiguration
            .apply(pivotClosedLoopConfiguration)
            .apply(pivotEncoderConfiguration)
            .inverted(PIVOT_INVERT)
            .smartCurrentLimit((int) PIVOT_CURRENT_LIMIT_STALL.magnitude(),
                               (int) PIVOT_CURRENT_LIMIT_FREE.magnitude())
            .idleMode(PIVOT_IDLE_MODE);
        distanceSensorConfiguration
            .withToFParams(new ToFParamsConfigs()
                .withUpdateMode(DISTANCE_SENSOR_UPDATE_MODE))
            .withProximityParams(new ProximityParamsConfigs()
                .withMinSignalStrengthForValidMeasurement(DISTANCE_SENSOR_SIGNAL_STRENGTH_THRESHOLD)
                .withProximityThreshold(DISTANCE_SENSOR_PROXIMITY_THRESHOLD));

        SparkMaxConfigUtil.configure(leftIntakeMotor, leftIntakeMotorConfiguration, "Coral left intake motor");
        SparkMaxConfigUtil.configure(rightIntakeMotor, rightIntakeMotorConfiguration, "Coral right intake motor");
        SparkMaxConfigUtil.configure(pivotMotor, pivotMotorConfiguration, "Coral pivot motor");
        distanceSensor.getConfigurator().apply(distanceSensorConfiguration);

        pivotClosedLoopController = pivotMotor.getClosedLoopController();
        pivotEncoder = pivotMotor.getAbsoluteEncoder();
        distanceSensorDistanceSignal = distanceSensor.getDistance();
        distanceSensorIsDetectedSignal = distanceSensor.getIsDetected();

        //Preferences.initDouble("coral tuning kG", PivotClosedLoopGains.G);
        //Preferences.initDouble("coral tuning kS", PivotClosedLoopGains.P);
        //Preferences.initDouble("coral tuning setpoint", 0);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("pivot angle", getPivotAngle());

        //closed loop controls
        double error = currentPivotSetpoint - getPivotAngle();
        double appliedP = error * PivotClosedLoopGains.P; 
        double fS = Math.signum(error) * PivotClosedLoopGains.S;
        double fG = Math.cos(Units.rotationsToRadians(getPivotAngle())) * PivotClosedLoopGains.G;
        //TODO (re)move
        SmartDashboard.putNumber("error", error);
        SmartDashboard.putNumber("appliedP", appliedP);
        SmartDashboard.putNumber("applied fS", fS);
        SmartDashboard.putNumber("applied fG", fG);
        if (pivotClosedLoopModeActive) {
            pivotMotor.setVoltage(MathUtil.clamp(MathUtil.clamp(appliedP, -4, 4) + fS + fG, -12, 12));
        }
    }

    // public Command bruhCmd(DoubleSupplier sigma) {
    //     return run(() -> {
    //         double input = sigma.getAsDouble() * 4;
    //         SmartDashboard.putNumber("input voltage", input);
    //         double fS = Math.signum(input) * Preferences.getDouble("coral tuning kS", 0);
    //         SmartDashboard.putNumber("applied fS", fS);
    //         double fG = Math.cos(Units.rotationsToRadians(getPivotAngle())) * Preferences.getDouble("coral tuning kG", 0);
    //         SmartDashboard.putNumber("applied fG", fG);
    //         pivotMotor.setVoltage(input + fS + fG);
    //     });
    //     return run(() -> {
    //         double setpoint = Preferences.getDouble("coral tuning setpoint", 0);
    //         double error = setpoint - getPivotAngle();
    //         SmartDashboard.putNumber("error", error);
    //         double fS = Math.signum(error) * Preferences.getDouble("coral tuning kS", 0);
    //         SmartDashboard.putNumber("applied fS", fS);
    //         double fG = Math.cos(Units.rotationsToRadians(getPivotAngle())) * Preferences.getDouble("coral tuning kG", 0);
    //         SmartDashboard.putNumber("applied fG", fG);
    //         pivotClosedLoopController.setReference(setpoint, ControlType.kPosition, ClosedLoopSlot.kSlot0, fS + fG);
    //     }).finallyDo(() -> pivotMotor.stopMotor());
    // }

    /**
     * Commands the pivot motor's closed-loop controller to move
     * the pivot mechanism to the given position.
     * @param rotations the angle to move the pivot mechanism to, in rotations. 
     */
    public void setPivotSetpoint(double rotations) {
        //pivotClosedLoopController.setReference(rotations, ControlType.kPosition);
        //no way to get the setpoint from SparkClosedLoopController.
        //fine, i'll do it myself!
        currentPivotSetpoint = rotations;
        pivotClosedLoopModeActive = true;
    }

    /**
     * Runs the pivot motor and rotates the coral mechanism manually.
     * Driver shouldn't have to do this in an actual match.
     * @param volts the voltage to run the pivot motor at. [-12, 12]
     * Positive values raise the coral mechanism and negative values lower it.
     */
    public void runPivotOpenLoop(double volts) {
        pivotMotor.setVoltage(volts);
        pivotClosedLoopModeActive = false;
    }

    /**
     * Runs the intake motors.
     * @param volts the voltage to run the motors at. [-12, 12]
     * Positive values suck the coral in and negative values spit the coral out.
     */
    public void runIntake(double volts) {
        leftIntakeMotor.setVoltage(volts);
    }

    /**
     * Stops the intake motors. Equivalent to {@code runIntake(0);}
     */
    public void stopIntake() {
        leftIntakeMotor.stopMotor();
    }

    /**
     * Stops the pivot motor. Interrupts closed-loop control.
     * Equivalent to {@code runPivotOpenLoop(0);}
     */
    public void stopPivot() {
        pivotMotor.stopMotor();
        pivotClosedLoopModeActive = false;
    }

    //Adamya
    public void resetToStraight() {
        setPivotSetpoint(STRAIGHT_ANGLE);
    }

    /**
     * @return the exact distance the distance sensor currently detects, in <b>meters</b>.
     */
    @Logged
    public double getDistanceSensorDistance() {
        return distanceSensorDistanceSignal.refresh().getValueAsDouble();
    }

    /**
     * @return true if the distance sensor detects coral in the intake.
     */
    @Logged(importance = Importance.CRITICAL)
    public boolean getCoralDetected() {
        if (Robot.isReal()) {
            return distanceSensorIsDetectedSignal.refresh().getValue();
        } else {
            return false;
        }
    }

    @Logged
    public boolean getAtSetpoint() {
        return Math.abs(currentPivotSetpoint - getPivotAngle()) <= PIVOT_AT_SETPOINT_TOLERANCE; 
    }

    /**
     * @return the current angle of the coral mechanism, in <b>rotations</b>.
     * (-0.5, 0.5].
     */
    @Logged(importance = Importance.CRITICAL)
    public double getPivotAngle() {
        return pivotEncoder.getPosition();
    }
}