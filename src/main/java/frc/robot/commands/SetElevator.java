package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;
import frc.robot.Enums.ElevatorEnums.*;
public class SetElevator extends Command {

    Supplier<ElevatorPosition> position;
    Elevator elevator;
    SetElevatorCancelBehavior cancelBehavior;



    
    public SetElevator(Elevator _elevator, Supplier<ElevatorPosition> _position, SetElevatorCancelBehavior cancelBehavior) {
        this.cancelBehavior = cancelBehavior;
        position = _position;
        elevator = _elevator;
        addRequirements(elevator);
        setName("Set Elevator to some position idk");
    }


    public SetElevator(Elevator elevator, ElevatorPosition position, SetElevatorCancelBehavior cancelBehavior) {
        this(elevator, () -> position, cancelBehavior);
        elevator.updateTarget(position); //This was the only addition, really
        setName("Set Elevator to " + position);
    }

    @Override
    public void initialize(){
        //TODO address this, seems redundant
        elevator.setPosition(position.get().getPosition());
    }

    @Override
    public boolean isFinished() {
        if (cancelBehavior == SetElevatorCancelBehavior.CANCEL_SETPOINT_REACHED) {
            return elevator.isAtSetpoint();
        } else {
            return true;
        }
    }
}
