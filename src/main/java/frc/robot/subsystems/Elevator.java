package frc.robot.subsystems;

import java.util.HashMap;

import com.ctre.phoenix6.configs.TalonFXConfigurator;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.DeviceIdentifier;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase{

    TalonFX motorController;

    // TODO: replace these with actual values
    double[] positions = {0, 0, 0, 0, 0, 0, 0};

    public Elevator(){
        this.setDefaultCommand(null);
    }

    public void setElevator(double rawPosition){
        // TODO: do something to the position
        motorController.setControl(new PositionVoltage(rawPosition));
    }

    public double getRawPosition(ElevatorPosition position) {
        switch (position) {
            case CORAL_1:
                return positions[0];
            case CORAL_2:
                return positions[1];
            case CORAL_3: 
                return positions[2];
            case CORAL_4:
                return positions[3];
            case ALGAE_1:
                return positions[4];
            case ALGAE_2:
                return positions[5];
            case DEFAULT:
                return positions[6];
            default:
                return positions[6];
            }      
    }

    public void configureMotor() {
        motorController = new TalonFX(0);
        TalonFXConfigurator configurator = new TalonFXConfigurator(new DeviceIdentifier(motorController.getDeviceID(), getSubsystem(), getName()));
    }
}
