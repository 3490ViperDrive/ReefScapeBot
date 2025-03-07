package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;
import frc.robot.Enums.*;
//import frc.robot.subsystems.Elevator.Positions;

public class SetElevator extends Command {

    Positions position;
    Elevator elevator;
    public SetElevator(Positions _position, Elevator _elevator){
        position = _position;
        elevator = _elevator;
        addRequirements(elevator);
    }
    @Override
    public void execute(){
        elevator.setElevator(position.get());
    }
}
