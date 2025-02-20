package frc.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.revrobotics.REVLibError;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.HardwareIds;

import static edu.wpi.first.units.Units.*;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;

public class CoralMechanism extends SubsystemBase {
    
    @Logged
    private final TalonSRX pivotMotor;
    @Logged
    private final SparkMax leftIntakeMotor;
    @Logged
    private final SparkMax rightIntakeMotor;

    private final Alert motorConfigurationAlerts =
        new Alert("Error while configuring coral mechanism motors:", AlertType.kError);

    public static final boolean INTAKE_INVERT_LEFT = false;
    public static final boolean INTAKE_INVERT_RIGHT = true;
    //these are guessed
    public static final Current INTAKE_CURRENT_LIMIT_FREE = Amps.of(40);
    public static final Current INTAKE_CURRENT_LIMIT_STALL = Amps.of(20);

    public static final IdleMode INTAKE_NEUTRAL_MODE = IdleMode.kBrake;

    public static final ResetMode INTAKE_CONFIG_RESET_MODE = ResetMode.kResetSafeParameters;
    public static final PersistMode INTAKE_CONFIG_PERSIST_MODE = PersistMode.kPersistParameters;

    public static final InvertType PIVOT_INVERT = InvertType.None;

    public static final Current PIVOT_CURRENT_LIMIT = Amps.of(40);

    public static final NeutralMode PIVOT_NEUTRAL_MODE = NeutralMode.Brake;

    //todo configure pivot talonsrx appropriately for abs encoder

    public CoralMechanism() {
        pivotMotor = new TalonSRX(HardwareIds.Can.CORAL_PIVOT_MOTOR);
        leftIntakeMotor = new SparkMax(HardwareIds.Can.CORAL_LEFT_INTAKE_MOTOR, MotorType.kBrushless);
        rightIntakeMotor = new SparkMax(HardwareIds.Can.CORAL_RIGHT_INTAKE_MOTOR, MotorType.kBrushless);

        //make motor configurations
        final SparkMaxConfig leftIntakeMotorConfiguration = new SparkMaxConfig();
        final SparkMaxConfig rightIntakeMotorConfiguration = new SparkMaxConfig();
        final TalonSRXConfiguration pivotMotorConfiguration = new TalonSRXConfiguration();
        leftIntakeMotorConfiguration
            .inverted(INTAKE_INVERT_LEFT)
            .smartCurrentLimit((int) INTAKE_CURRENT_LIMIT_STALL.magnitude(),
                               (int) INTAKE_CURRENT_LIMIT_FREE.magnitude())
            .idleMode(INTAKE_NEUTRAL_MODE);
        rightIntakeMotorConfiguration
            .apply(leftIntakeMotorConfiguration)
            .inverted(INTAKE_INVERT_RIGHT)
            //add this boolean argument back if it spins the wrong way
            .follow(leftIntakeMotor/*, true*/); 

        pivotMotorConfiguration.peakCurrentLimit = (int) PIVOT_CURRENT_LIMIT.magnitude();
        pivotMotorConfiguration.continuousCurrentLimit = (int) PIVOT_CURRENT_LIMIT.magnitude();

        //configure motors
        final REVLibError leftConfigStatus, rightConfigStatus;
        final ErrorCode pivotConfigStatus;
        leftConfigStatus = leftIntakeMotor.configure(leftIntakeMotorConfiguration,
                                                     INTAKE_CONFIG_RESET_MODE,
                                                     INTAKE_CONFIG_PERSIST_MODE);
        rightConfigStatus = rightIntakeMotor.configure(rightIntakeMotorConfiguration,
                                                       INTAKE_CONFIG_RESET_MODE,
                                                       INTAKE_CONFIG_PERSIST_MODE);
        pivotConfigStatus = pivotMotor.configAllSettings(pivotMotorConfiguration);
        //certain Talon SRX configurations aren't included within TalonSRXConfiguration
        pivotMotor.setInverted(PIVOT_INVERT);
        pivotMotor.setNeutralMode(PIVOT_NEUTRAL_MODE);
        
        //report any configuration errors
        if (leftConfigStatus != REVLibError.kOk) {
            motorConfigurationAlerts.setText(motorConfigurationAlerts.getText()
                                             + "\nLeft intake motor: "
                                             //calls substring(1) to remove the k at the start of every
                                             //REVLibError name
                                             + leftConfigStatus.toString().substring(1));
            motorConfigurationAlerts.set(true);
        }
        if (rightConfigStatus != REVLibError.kOk) {
            motorConfigurationAlerts.setText(motorConfigurationAlerts.getText()
                                             + "\nRight intake motor: "
                                             + leftConfigStatus.toString().substring(1));
            motorConfigurationAlerts.set(true);
        }
        if (pivotConfigStatus != ErrorCode.OK) {
            motorConfigurationAlerts.setText(motorConfigurationAlerts.getText()
                                             + "\nPivot motor: "
                                             + pivotConfigStatus);
            motorConfigurationAlerts.set(true);
        }
    }

    @Logged
    public String getMotorConfigurationAlerts() {
        if (motorConfigurationAlerts.get()) {
            return motorConfigurationAlerts.getText();
        } else {
            return "No motor configuration errors reported";
        }
        
    }

    //todo add move to setpoint
    //todo add run intake
}
