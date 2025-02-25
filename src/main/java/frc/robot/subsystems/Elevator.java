package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.DeviceIdentifier;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase{

    TalonFX motorController;

    public Elevator(){
        this.setDefaultCommand(null);
    }

    public void setElevator(double position){
        motorController.setControl(new PositionVoltage(position));
    }

    public void configureMotor() {
        motorController = new TalonFX(0);
        TalonFXConfigurator configurator = new TalonFXConfigurator(new DeviceIdentifier(motorController.getDeviceID(), getSubsystem(), getName()));
    }
}
