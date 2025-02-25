package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
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
import edu.wpi.first.units.measure.Current;

public class CoralMechanism extends SubsystemBase {
    
    @Logged
    private final SparkMax leftIntakeMotor;
    @Logged
    private final SparkMax rightIntakeMotor;
    @Logged
    private final SparkMax pivotMotor;
    private final SparkClosedLoopController pivotClosedLoopController;

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

    //TODO ensure the pivot motor is in phase with the pivot encoder; if not, add an EncoderConfig
    public static final FeedbackSensor PIVOT_FEEDBACK_SENSOR = FeedbackSensor.kAbsoluteEncoder;

    public CoralMechanism() {
        //TODO make sure the chosen pivot motor (brushed/brushless) is accounted for here
        pivotMotor = new SparkMax(HardwareIds.Can.CORAL_PIVOT_MOTOR, MotorType.kBrushed);
        leftIntakeMotor = new SparkMax(HardwareIds.Can.CORAL_LEFT_INTAKE_MOTOR, MotorType.kBrushless);
        rightIntakeMotor = new SparkMax(HardwareIds.Can.CORAL_RIGHT_INTAKE_MOTOR, MotorType.kBrushless);

        //make motor configurations
        final SparkMaxConfig leftIntakeMotorConfiguration = new SparkMaxConfig();
        final SparkMaxConfig rightIntakeMotorConfiguration = new SparkMaxConfig();
        final ClosedLoopConfig pivotClosedLoopConfiguration = new ClosedLoopConfig();
        final SparkMaxConfig pivotMotorConfiguration = new SparkMaxConfig();
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
        pivotMotorConfiguration
            .apply(pivotClosedLoopConfiguration)
            .inverted(PIVOT_INVERT)
            .smartCurrentLimit((int) PIVOT_CURRENT_LIMIT_STALL.magnitude(),
                               (int) PIVOT_CURRENT_LIMIT_FREE.magnitude())
            .idleMode(PIVOT_IDLE_MODE);

        SparkMaxConfigUtil.configure(leftIntakeMotor, leftIntakeMotorConfiguration, "Coral left intake motor");
        SparkMaxConfigUtil.configure(rightIntakeMotor, rightIntakeMotorConfiguration, "Coral right intake motor");
        SparkMaxConfigUtil.configure(pivotMotor, pivotMotorConfiguration, "Coral pivot motor");

        pivotClosedLoopController = pivotMotor.getClosedLoopController();
    }

    /**
     * Commands the pivot motor's closed-loop controller to move
     * the pivot mechanism to the given position.
     * @param rotations the angle to move the pivot mechanism to, in rotations. 
     */
    public void setPivotSetpoint(double rotations) {
        pivotClosedLoopController.setReference(rotations, ControlType.kPosition);
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
}