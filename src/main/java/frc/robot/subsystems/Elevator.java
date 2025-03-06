package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.DeviceIdentifier;
import com.ctre.phoenix6.hardware.TalonFX;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase{

    TalonFX motorController;
    public enum Positions{
        // TODO: set values 
        DEFAULT(0.0),
        ALGAE_L2(0.0),
        ALGAE_L3(0.0),
        CORAL_L1(0.0),
        CORAL_L2(0.0),
        CORAL_L3(0.0),
        CORAL_L4(0.0);

        double position;

        Positions(double position){
            this.position = position;
        }

        public double get(){
            return this.position;
        }
    }

    public void setElevator(double rawPosition){
        // TODO: do something to the position
        motorController.setControl(new PositionVoltage(rawPosition));
    }

    public void configureMotor() {
        motorController = new TalonFX(0);
        TalonFXConfigurator configurator = new TalonFXConfigurator(new DeviceIdentifier(motorController.getDeviceID(), getSubsystem(), getName()));
    }
}
