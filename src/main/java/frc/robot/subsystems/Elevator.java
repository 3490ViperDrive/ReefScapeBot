package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.DeviceIdentifier;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase{

    TalonFX motorController;
    int target;


    
    public Elevator(){
        target = 0; //Stowed
    }

    public void setElevator(double rawPosition){
        motorController.setControl(new PositionVoltage(rawPosition));
    }

    public void setTarget(int i){
        target = i;
        //TODO any dashboard piping will go here
    }

    public void incrementTarget(){
        
    }

    public void configureMotor() {
        motorController = new TalonFX(0);
        TalonFXConfigurator configurator = new TalonFXConfigurator(new DeviceIdentifier(motorController.getDeviceID(), getSubsystem(), getName()));
    }

}
