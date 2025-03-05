package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.AbsoluteEncoderConfig;
import com.revrobotics.spark.config.ClosedLoopConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.HardwareIds;
import frc.robot.utils.SparkMaxConfigUtil;

public class AlgaeMechanism extends SubsystemBase {

    SparkMax algaeMotorLeft;
    SparkMax algaeMotorRight;
    SparkMax algaePivotMotor;

    SparkClosedLoopController algaeClosedLoopController;

    AbsoluteEncoder algaePivotEncoder;

    double currentAlgaePivotSetpoint;

    boolean algaePivotClosedLoopModeActive;

    boolean ALGAE_INVERT_LEFT = false;
    boolean ALGAE_INVERT_RIGHT = true;
    boolean ALGAE_INVERT_FOLLOWER = true;

    // TODO put real numbers
    Current ALGAE_CURRENT_LIMIT_FREE = Amps.of(0);
    Current ALGAE_CURRENT_LIMIT_STALL = Amps.of(0);

    IdleMode ALGAE_IDLE_MODE = IdleMode.kBrake;

    boolean ALGAE_PIVOT_INVERT = false;

    // TODO put real numbers
    Current ALGAE_PIVOT_CURRENT_LIMIT_FREE = Amps.of(0);
    Current ALGAE_PIVOT_CURRENt_LIMIT_STALL = Amps.of(0);

    IdleMode ALGAE_PIVOT_IDLE_MODE = IdleMode.kBrake;

    // TODO put real numbers
    public static class AlgaeClosedLoopGains {
        public static final double P = 0; // volts per rotation of error
        public static final double D = 0; // volts per rotation of error per second
        public static final double G = 0;
        public static final double S = 0;
        public static final double V = 0; //could be unnecessary
    }

      // TODO Put the real threshod value
      double ALGAE_INTAKE_SPEED = 12;
      public static final double thresholdValue = 0;

    FeedbackSensor ALGAE_FEEDBACK_SENSOR = FeedbackSensor.kAbsoluteEncoder;

    //TODO set zero offset such that zero angle aligns with horizontal
    Angle ALGAE_ENCODER_ZERO_OFFSET = Rotations.of(0);
    boolean ALGAE_ENCODER_ZERO_CENTERED = true;
    //TODO ensure the algae motor is in phase with the pivot encoder
    boolean ALGAE_ENCODER_INVERT = false;

    double ALGAE_AT_SETPOINT_TOLERANCE = 0.02; //1/50th of a rotation in either direction

     public AlgaeMechanism() {
        algaeMotorLeft = new SparkMax(HardwareIds.Can.ALGAE_INTAKE_MOTOR_LEFT, MotorType.kBrushless);
        algaeMotorRight = new SparkMax(HardwareIds.Can.ALGAE_INTAKE_MOTOR_RIGHT, MotorType.kBrushless);
        algaePivotMotor = new SparkMax(HardwareIds.Can.ALGAE_PIVOT_MOTOR, MotorType.kBrushless);
        currentAlgaePivotSetpoint = 0;
        algaePivotClosedLoopModeActive = false;

        SparkMaxConfig algaeMotorLeftConfiguration = new SparkMaxConfig();
        SparkMaxConfig alageMotorRightConfiguration = new SparkMaxConfig();
        ClosedLoopConfig algaePivotClosedLoopConfiguration = new ClosedLoopConfig();
        SparkMaxConfig algaePivotMotorConfiguration = new SparkMaxConfig();
        AbsoluteEncoderConfig alagePivotEncoderConfiguration = new AbsoluteEncoderConfig();

        algaeMotorLeftConfiguration.inverted(ALGAE_INVERT_LEFT)
        .smartCurrentLimit((int) ALGAE_CURRENT_LIMIT_STALL.magnitude(),
        (int) ALGAE_CURRENT_LIMIT_FREE.magnitude())
        .idleMode(ALGAE_IDLE_MODE);

        alageMotorRightConfiguration.apply(algaeMotorLeftConfiguration)
        .inverted(ALGAE_INVERT_RIGHT)
        //add this boolean argument back if it spins the wrong way
        .follow(algaeMotorLeft, ALGAE_INVERT_FOLLOWER);

        algaePivotClosedLoopConfiguration
        .p(AlgaeClosedLoopGains.P)
        .d(AlgaeClosedLoopGains.D)
        .feedbackSensor(ALGAE_FEEDBACK_SENSOR);

        alagePivotEncoderConfiguration.inverted(ALGAE_ENCODER_INVERT)
        .zeroOffset(ALGAE_ENCODER_ZERO_OFFSET.in(Rotations))
        .zeroCentered(ALGAE_ENCODER_ZERO_CENTERED);

        algaePivotMotorConfiguration.apply(algaePivotClosedLoopConfiguration)
        .apply(alagePivotEncoderConfiguration)
        .inverted(ALGAE_PIVOT_INVERT)
        .smartCurrentLimit((int) ALGAE_CURRENT_LIMIT_STALL.magnitude(),
        (int) ALGAE_CURRENT_LIMIT_FREE.magnitude())
        .idleMode(ALGAE_IDLE_MODE);

        SparkMaxConfigUtil.configure(algaeMotorLeft, algaeMotorLeftConfiguration, "Left algae intake motor");
        SparkMaxConfigUtil.configure(algaeMotorRight, alageMotorRightConfiguration, "Right algae intake motor");
        SparkMaxConfigUtil.configure(algaePivotMotor, algaePivotMotorConfiguration, "Algae pivot motor");

        algaeClosedLoopController = algaePivotMotor.getClosedLoopController();
        algaePivotEncoder = algaePivotMotor.getAbsoluteEncoder();
     }

     @Override
     public void periodic() {
      checkIntakeCurrent(); 
      
      SmartDashboard.putNumber("algae angle", getAlgaePivotAngle());

      double error = currentAlgaePivotSetpoint - getAlgaePivotAngle();
      double appliedP = error * AlgaeClosedLoopGains.P;
      double fV = algaePivotEncoder.getVelocity()*AlgaeClosedLoopGains.V;
      double fS = Math.signum(error) * AlgaeClosedLoopGains.S;
      double fG = Math.cos(Units.rotationsToRadians(getAlgaePivotAngle())) * AlgaeClosedLoopGains.G;

      // TODO put real numbers
      if (algaePivotClosedLoopModeActive) {
         algaePivotMotor.setVoltage(MathUtil.clamp(MathUtil.clamp(appliedP, 0, 0) + fS + fG + MathUtil.clamp(fV, 0, 0), 0, 0));
      }
     }

     public void setAlgaePivotSetpoint(double rotations) {
      currentAlgaePivotSetpoint = rotations;
      algaePivotClosedLoopModeActive = true;
     }

     public void runAlgaePivot(double volts) {
        algaePivotMotor.setVoltage(volts);
        algaePivotClosedLoopModeActive = false;
     }

     public void runAlgaeIntake(double volts) {
        algaeMotorLeft.setVoltage(volts);
     }

     public void stopAlgaeIntake() {
        algaeMotorLeft.stopMotor();
     }

     public void stopAlgaePivot() {
        algaePivotMotor.stopMotor();
        algaePivotClosedLoopModeActive = false;
     }

     double getAlgaePivotAngle() {
        return algaePivotEncoder.getPosition();
     }

     public boolean getAtAlgaeSetpoint(){
      return Math.abs(currentAlgaePivotSetpoint - getAlgaePivotAngle()) <= ALGAE_AT_SETPOINT_TOLERANCE;
     }

     public boolean checkIntakeCurrent() {
     double currentDraw = algaeMotorLeft.getOutputCurrent();

      if (currentDraw > thresholdValue) {
         return true;
         //stopAlgaeIntake();
         //System.out.println("Algae detected, stopping intake");
      } else {
         return false;
      }
     }
   //TODO: Actually find the numbers
}   
