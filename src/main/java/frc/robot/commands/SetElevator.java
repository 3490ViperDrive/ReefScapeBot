package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.ElevatorPosition;

public class SetElevator extends Command {
    ElevatorPosition position;
    Elevator elevator;
    public SetElevator(ElevatorPosition _position, Elevator _elevator){
        position = _position;
        elevator = _elevator;
    }
    @Override
    public void execute(){
        elevator.setElevator(elevator.getRawPosition(position));
    }
}
