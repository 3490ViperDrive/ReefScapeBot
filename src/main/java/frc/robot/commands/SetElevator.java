package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevator;
import frc.robot.Enums.ElevatorEnums.*;
public class SetElevator extends Command {

    //not the solution that we wanted, but the solution we needed.
    //TODO I'm not sure about that last bit, chief
    Supplier<ElevatorPosition> position;
    Elevator elevator;
    SetElevatorCancelBehavior cancelBehavior;

    //TODO overly-verbose enum names are overly-verbose
    public enum SetElevatorCancelBehavior {
        CANCEL_IMMEDIATELY,
        CANCEL_SETPOINT_REACHED
    }

    //TODO Take the supplier goggles off; there is no need for this to require a supplier; just pass in the elevator position
    public SetElevator(Elevator _elevator, Supplier<ElevatorPosition> _position, SetElevatorCancelBehavior cancelBehavior) {
        this.cancelBehavior = cancelBehavior;
        position = _position;
        elevator = _elevator;
        addRequirements(elevator);
        setName("Set Elevator to some position idk");
    }


    public SetElevator(Elevator elevator, ElevatorPosition position, SetElevatorCancelBehavior cancelBehavior) {
        this(elevator, () -> position, cancelBehavior);
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
