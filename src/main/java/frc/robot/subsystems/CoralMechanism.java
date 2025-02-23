package frc.robot.subsystems;

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
    private final SparkMax pivotMotor;
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

    public static final IdleMode INTAKE_IDLE_MODE = IdleMode.kBrake;

    public static final ResetMode CONFIG_RESET_MODE = ResetMode.kResetSafeParameters;
    public static final PersistMode CONFIG_PERSIST_MODE = PersistMode.kPersistParameters;

    public static final boolean PIVOT_INVERT = false;

    public static final Current PIVOT_CURRENT_LIMIT_FREE = Amps.of(50);
    public static final Current PIVOT_CURRENT_LIMIT_STALL = Amps.of(30);

    public static final IdleMode PIVOT_IDLE_MODE = IdleMode.kBrake;

    //todo configure pivot sparkmax control loop

    public CoralMechanism() {
        //TODO make sure the chosen pivot motor (brushed/brushless) is accounted for here
        pivotMotor = new SparkMax(HardwareIds.Can.CORAL_PIVOT_MOTOR, MotorType.kBrushed);
        leftIntakeMotor = new SparkMax(HardwareIds.Can.CORAL_LEFT_INTAKE_MOTOR, MotorType.kBrushless);
        rightIntakeMotor = new SparkMax(HardwareIds.Can.CORAL_RIGHT_INTAKE_MOTOR, MotorType.kBrushless);

        //make motor configurations
        final SparkMaxConfig leftIntakeMotorConfiguration = new SparkMaxConfig();
        final SparkMaxConfig rightIntakeMotorConfiguration = new SparkMaxConfig();
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
        pivotMotorConfiguration
            .inverted(PIVOT_INVERT)
            .smartCurrentLimit((int) PIVOT_CURRENT_LIMIT_STALL.magnitude(),
                               (int) PIVOT_CURRENT_LIMIT_FREE.magnitude())
            .idleMode(PIVOT_IDLE_MODE);

        configureRevMotor(leftIntakeMotor, leftIntakeMotorConfiguration, "Left intake motor");
        configureRevMotor(rightIntakeMotor, rightIntakeMotorConfiguration, "Right intake motor");
        configureRevMotor(pivotMotor, pivotMotorConfiguration, "Pivot motor");
    }

    /**
     * Configures a SparkMAX with the given SparkMaxConfig. If the configuration fails,
     * reports the error to the dashboard.
     * @param motorController the SparkMAX to configure
     * @param configuration the configuration to apply
     * @param name the name of the motor/motor controller to report to the dashboard if an error occurs
     */
    private void configureRevMotor(SparkMax motorController, SparkMaxConfig configuration, String name) {
        final REVLibError configStatus;
        configStatus = leftIntakeMotor.configure(configuration, CONFIG_RESET_MODE, CONFIG_PERSIST_MODE);
        if (configStatus != REVLibError.kOk) {
            motorConfigurationAlerts.setText(
                String.format("%s\n%s: %s",
                              motorConfigurationAlerts.getText(),
                              name,
                              configStatus.toString().substring(1)));
            motorConfigurationAlerts.set(true);
        }
    }

    /**
     * Configures a SparkMAX with the given SparkMaxConfig. If the configuration fails,
     * reports the error to the dashboard. Uses a default name for the SparkMAX (SparkMAX with CAN ID x)
     * @param motorController the SparkMAX to configure
     * @param configuration the configuration to apply
     */
    private void configureRevMotor(SparkMax motorController, SparkMaxConfig configuration) {
        configureRevMotor(motorController,
                          configuration,
                          String.format("SparkMAX with CAN ID %d", motorController.getDeviceId()));
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

    //todo add manual move

    /**
     * Runs the intake motors.
     * @param volts the voltage to run the motors at. [-12, 12]
     * Positive values suck the coral in and negative values spit the coral out.
     */
    public void runIntake(double volts) {
        leftIntakeMotor.setVoltage(volts);
    }

    /**
     * Stops the intake motors. Equivalent to runIntake(0);
     */
    public void stopIntake() {
        leftIntakeMotor.stopMotor();
    }
}
