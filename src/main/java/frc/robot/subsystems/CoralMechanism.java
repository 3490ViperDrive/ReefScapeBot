package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.configs.ProximityParamsConfigs;
import com.ctre.phoenix6.configs.ToFParamsConfigs;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.signals.UpdateModeValue;
import com.revrobotics.spark.SparkBase.ControlType;
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
import frc.robot.utils.SparkMaxConfigUtil;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.epilogue.Logged.Importance;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Distance;

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

    public static final boolean INTAKE_INVERT_LEFT = false;
    public static final boolean INTAKE_INVERT_RIGHT = true;

    //these are guessed
    public static final Current INTAKE_CURRENT_LIMIT_FREE = Amps.of(40);
    public static final Current INTAKE_CURRENT_LIMIT_STALL = Amps.of(20);

    public static final IdleMode INTAKE_IDLE_MODE = IdleMode.kBrake;

    public static final boolean PIVOT_INVERT = false;

    //these are also guessed
    public static final Current PIVOT_CURRENT_LIMIT_FREE = Amps.of(50);
    public static final Current PIVOT_CURRENT_LIMIT_STALL = Amps.of(30);

    public static final IdleMode PIVOT_IDLE_MODE = IdleMode.kBrake;

    //TODO tune
    public static class PivotClosedLoopGains {
        public static final double P = 0; //volts per rotation of error
        public static final double D = 0; //volts per rotation of error per second
        //gravity feedforward term kG may be necessary, but cannot be implemented
        //via rev closed loop controller config
    }

    
    public static final FeedbackSensor PIVOT_FEEDBACK_SENSOR = FeedbackSensor.kAbsoluteEncoder;

    //TODO set zero offset such that zero angle aligns with horizontal
    public static final Angle PIVOT_ENCODER_ZERO_OFFSET = Rotations.of(0);
    public static final boolean PIVOT_ENCODER_ZERO_CENTERED = true;
    //TODO ensure the pivot motor is in phase with the pivot encoder
    public static final boolean PIVOT_ENCODER_INVERT = false;

    public static final UpdateModeValue DISTANCE_SENSOR_UPDATE_MODE = UpdateModeValue.ShortRange100Hz;
    public static final double DISTANCE_SENSOR_SIGNAL_STRENGTH_THRESHOLD = 2700;
    public static final double DISTANCE_SENSOR_PROXIMITY_THRESHOLD = 0.25;

    public CoralMechanism() {
        //TODO make sure the chosen pivot motor (brushed/brushless) is accounted for here
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
            //add this boolean argument back if it spins the wrong way
            .follow(leftIntakeMotor/*, true*/); 
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
    }

    /**
     * Commands the pivot motor's closed-loop controller to move
     * the pivot mechanism to the given position.
     * @param rotations the angle to move the pivot mechanism to, in rotations. 
     */
    public void setPivotSetpoint(double rotations) {
        pivotClosedLoopController.setReference(rotations, ControlType.kPosition);
        //no way to get the setpoint from SparkClosedLoopController.
        //fine, i'll do it myself!
        currentPivotSetpoint = rotations;
        pivotClosedLoopModeActive = true;
    }

    //TODO confirm a positive value results in a rotation that brings the mechanism up
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
        return distanceSensorIsDetectedSignal.refresh().getValue();
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