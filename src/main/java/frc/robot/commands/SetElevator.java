package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;

public class SetElevator extends Command {
    double position;
    Elevator elevatorSubsystem;
    public SetElevator(int _position, Elevator _elevatorSubsystem){
        position = _position;
        elevatorSubsystem = _elevatorSubsystem;
    }
    @Override
    public void execute(){
        elevatorSubsystem.setElevator(position);
    }
}
