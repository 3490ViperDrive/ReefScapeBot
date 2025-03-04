package frc.robot.utils;

import com.revrobotics.REVLibError;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;

public class SparkMaxConfigUtil {

    private SparkMaxConfigUtil() {}
    
    public static final ResetMode CONFIG_RESET_MODE = ResetMode.kResetSafeParameters;
    public static final PersistMode CONFIG_PERSIST_MODE = PersistMode.kPersistParameters;

    private static final Alert motorConfigurationAlerts =
        new Alert("Error while configuring motors:", AlertType.kError);

        /**
     * Configures a SparkMAX with the given SparkMaxConfig. If the configuration fails,
     * reports the error to the dashboard.
     * @param motorController the SparkMAX to configure
     * @param configuration the configuration to apply
     * @param name the name of the motor/motor controller to report to the dashboard if an error occurs
     * @return true if the SparkMAX was configured successfully
     */
    public static boolean configure(SparkMax motorController, SparkMaxConfig configuration, String name) {
        final REVLibError configStatus;
        configStatus = motorController.configure(configuration, CONFIG_RESET_MODE, CONFIG_PERSIST_MODE);
        if (configStatus != REVLibError.kOk) {
            motorConfigurationAlerts.setText(
                String.format("%s\n%s: %s",
                              motorConfigurationAlerts.getText(),
                              name,
                              configStatus.toString().substring(1)));
            motorConfigurationAlerts.set(true);
            return false;
        }
        return true;
    }

    /**
     * Configures a SparkMAX with the given SparkMaxConfig. If the configuration fails,
     * reports the error to the dashboard. Uses a default name for the SparkMAX (SparkMAX with CAN ID x)
     * @param motorController the SparkMAX to configure
     * @param configuration the configuration to apply
     */
    public static boolean configure(SparkMax motorController, SparkMaxConfig configuration) {
        return configure(motorController,
                         configuration,
                         String.format("SparkMAX with CAN ID %d", motorController.getDeviceId()));
    }

    public static String getMotorConfigurationAlerts() {
        if (motorConfigurationAlerts.get()) {
            return motorConfigurationAlerts.getText();
        } else {
            return "No motor configuration errors reported";
        }
    }
}
