package frc.robot.subsystems;

import java.util.HashMap;

import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.DeviceIdentifier;
import com.ctre.phoenix6.hardware.TalonFX;


import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase{

    TalonFX motorController;

    public HashMap<String,Double> elevatorPositions = new HashMap<String,Double>();

    public Elevator(){
        this.setDefaultCommand(null);
        //TODO: replace these with actual values
        elevatorPositions.put("Default", 0.0);
        elevatorPositions.put("Algae_1", 0.0);
        elevatorPositions.put("Algae_1", 0.0);
        elevatorPositions.put("Coral_1", 0.0);
        elevatorPositions.put("Coral_2", 0.0);
        elevatorPositions.put("Coral_3", 0.0);
        elevatorPositions.put("Coral_4", 0.0);
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
