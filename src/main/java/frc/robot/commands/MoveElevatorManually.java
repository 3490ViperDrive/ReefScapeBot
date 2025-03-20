package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;

public class MoveElevatorManually extends Command{
    
    Elevator _elevator;
    double retVal;

    public MoveElevatorManually(double val){
        _elevator = Elevator.instance;
        retVal = val;
        addRequirements(_elevator);

    }

    @Override
    public void execute(){
        _elevator.runOpenLoop(retVal);
    }

    @Override
    public void end(boolean isCanceled){
        _elevator.runOpenLoop(0);
    }
}
