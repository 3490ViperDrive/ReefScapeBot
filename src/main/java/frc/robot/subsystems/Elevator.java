package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.HardwareIds;
import static frc.robot.Enums.ElevatorEnums.*;

public class Elevator extends SubsystemBase {

    private final TalonFX motorController;

    public static final double SPOOL_DIAMETER = 1.37; //inches
    public static final double GEAR_RATIO = 25; //25:1

    //sensor to mechanism ratio; rotor rotations -> inches
    public static final double CONVERSION_FACTOR = 1 / ((Math.PI * SPOOL_DIAMETER) / GEAR_RATIO);

    public static final InvertedValue INVERT = InvertedValue.Clockwise_Positive; 
    public static final NeutralModeValue IDLE_MODE = NeutralModeValue.Brake;

    public static final double SUPPLY_CURRENT_LIMIT = 50; //amps

    public static final FeedbackSensorSourceValue FEEDBACK_SENSOR = FeedbackSensorSourceValue.RotorSensor;

    public static final GravityTypeValue GRAVITY_TYPE = GravityTypeValue.Elevator_Static;

    public static final double AT_SETPOINT_TOLERANCE = 0.5; //half an inch

    public TargetLevel currentTarget;

    public static class ClosedLoopGains {
        //feedback
        public static final double P = 6;
        //gravity feedforward (static)
        public static final double G = 0.16;
    }





    public Elevator(){
        currentTarget = TargetLevel.L1;

        motorController = new TalonFX(HardwareIds.Can.ELEVATOR_MOTOR);
        final TalonFXConfigurator configurator = motorController.getConfigurator();
        final TalonFXConfiguration configuration = new TalonFXConfiguration();
        final MotorOutputConfigs motorOutputConfiguration = new MotorOutputConfigs();
        final CurrentLimitsConfigs currentLimitsConfiguration = new CurrentLimitsConfigs();
        final FeedbackConfigs feedbackConfiguration = new FeedbackConfigs();
        final Slot0Configs slot0Configuration = new Slot0Configs();

        //TODO separating the declaration from definition in the same method call is questionable
        motorOutputConfiguration
            .withInverted(INVERT)
            .withNeutralMode(IDLE_MODE);
        currentLimitsConfiguration
            .withSupplyCurrentLimit(SUPPLY_CURRENT_LIMIT)
            .withSupplyCurrentLimitEnable(true);
        feedbackConfiguration
            .withFeedbackSensorSource(FEEDBACK_SENSOR)
            .withSensorToMechanismRatio(CONVERSION_FACTOR);
        slot0Configuration
            .withKP(ClosedLoopGains.P)
            .withKG(ClosedLoopGains.G);
        configuration
            .withMotorOutput(motorOutputConfiguration)
            .withFeedback(feedbackConfiguration)
            .withSlot0(slot0Configuration);

        configurator.apply(configuration);

        //this assumes that the elevator is at the lower hardstop and the rope is taut
        //this is bad!
        motorController.setPosition(0);
    }

    public void setPosition(double rawPosition) {
        motorController.setControl(new PositionVoltage(rawPosition));
    }

    public void runOpenLoop(double volts) {
        motorController.setControl(new VoltageOut(volts));
    }

    public boolean isAtSetpoint() {
        return Math.abs(motorController.getClosedLoopError().getValueAsDouble()) < AT_SETPOINT_TOLERANCE;
    }

    /** 
     *  Updates the current "Target Level" of the Elevator. For use in 'automated' elevator commands.
     * @param level
     */
    public void setTargetLevel(TargetLevel level) {
        this.currentTarget = level;
    }

    @Logged
    public TargetLevel getCurrentTarget() {
        return currentTarget;
    }
}
