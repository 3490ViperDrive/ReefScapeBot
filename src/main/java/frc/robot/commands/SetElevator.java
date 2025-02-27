package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;

public class SetElevator extends Command {

    String position;
    Elevator elevator;
    public SetElevator(String _position, Elevator _elevator){
        position = _position;
        elevator = _elevator;
    }
    @Override
    public void execute(){
        elevator.setElevator(elevator.elevatorPositions.get(position));
    }
}
