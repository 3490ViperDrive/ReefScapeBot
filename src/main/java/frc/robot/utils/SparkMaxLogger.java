package frc.robot.utils;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.epilogue.CustomLoggerFor;
import edu.wpi.first.epilogue.logging.ClassSpecificLogger;
import edu.wpi.first.epilogue.logging.EpilogueBackend;

@CustomLoggerFor(SparkMax.class)
public class SparkMaxLogger extends ClassSpecificLogger<SparkMax> {
    public SparkMaxLogger() {
        super(SparkMax.class);
    }

    @Override
    public void update(EpilogueBackend backend, SparkMax motorController) {
        RelativeEncoder encoder = motorController.getEncoder();
        backend.log("CAN ID", motorController.getDeviceId());
        backend.log("Has Faults", motorController.hasActiveFault());
        backend.log("Has Warnings", motorController.hasActiveWarning());
        backend.log("Applied Output", motorController.getAppliedOutput());
        backend.log("Motor Velocity (RPM)", encoder.getVelocity());
        backend.log("Motor Temp (Celsius)", motorController.getMotorTemperature());
        backend.log("Output Current (Amps)", motorController.getOutputCurrent());
    }
}
