package frc.robot.configs;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ElevatorConfigs {
    public static final double SPOOL_DIAMETER = 1.37; //inches
    public static final double GEAR_RATIO = 9; //9:1

    //sensor to mechanism ratio; rotor rotations -> inches
    public static final double CONVERSION_FACTOR = 1 / ((Math.PI * SPOOL_DIAMETER) / GEAR_RATIO);

    public static final InvertedValue INVERT = InvertedValue.Clockwise_Positive; 
    public static final NeutralModeValue IDLE_MODE = NeutralModeValue.Brake;

    public static final double SUPPLY_CURRENT_LIMIT = 50; //amps

    public static final FeedbackSensorSourceValue FEEDBACK_SENSOR = FeedbackSensorSourceValue.RotorSensor;
    public static final GravityTypeValue GRAVITY_TYPE = GravityTypeValue.Elevator_Static;

    public static final double AT_SETPOINT_TOLERANCE = 0.5; //half an inch
    public static class ClosedLoopGains {
        //feedback
        public static final double P = 6;
        //gravity feedforward (static)
        public static final double G = 0.16;

        public static final double CRUISE_VELOCITY = 90.34;
        public static final double ACCELERATION = 343; 
    }//ClosedLoopGains

    //TODO Pass in a more generalized motorController type as opposed to specifically the TalonFX
    public void configureMotor(TalonFX motorController){
        
        final TalonFXConfigurator configurator = motorController.getConfigurator();
        final TalonFXConfiguration configuration = new TalonFXConfiguration();
        final MotorOutputConfigs motorOutputConfiguration = new MotorOutputConfigs();
        final CurrentLimitsConfigs currentLimitsConfiguration = new CurrentLimitsConfigs();
        final FeedbackConfigs feedbackConfiguration = new FeedbackConfigs();
        final Slot0Configs slot0Configuration = new Slot0Configs();
        final MotionMagicConfigs motionMagicConfigs = new MotionMagicConfigs();

        //Questionable separation of declaration and configuration
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
        motionMagicConfigs
            .withMotionMagicAcceleration(ClosedLoopGains.ACCELERATION)
            .withMotionMagicCruiseVelocity(ClosedLoopGains.CRUISE_VELOCITY);
        configuration
            .withMotorOutput(motorOutputConfiguration)
            .withFeedback(feedbackConfiguration)
            .withSlot0(slot0Configuration)
            .withMotionMagic(motionMagicConfigs);

        configurator.apply(configuration);
    }
}
