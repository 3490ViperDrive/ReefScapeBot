package frc.robot.utils;

import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.epilogue.CustomLoggerFor;
import edu.wpi.first.epilogue.logging.ClassSpecificLogger;
import edu.wpi.first.epilogue.logging.EpilogueBackend;

@CustomLoggerFor(TalonSRX.class)
public class TalonSrxLogger extends ClassSpecificLogger<TalonSRX> {

    private final Faults faults = new Faults();

    public TalonSrxLogger() {
        super(TalonSRX.class);
    }

    @Override
    public void update(EpilogueBackend backend, TalonSRX motorController) {
        motorController.getFaults(faults);
        backend.log("CAN ID", motorController.getDeviceID());
        backend.log("Has Faults", faults.hasAnyFault());
        backend.log("Output Voltage (Volts)", motorController.getMotorOutputVoltage());
        backend.log("Motor Velocity (Tcs per ds)", motorController.getSelectedSensorVelocity());
        backend.log("Motor Temp (Celsius)", motorController.getTemperature());
        backend.log("Output Current (Amps)", motorController.getStatorCurrent());
    }
}
