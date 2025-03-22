package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Enums.ElevatorEnums.SetElevatorCancelBehavior;
import frc.robot.subsystems.CoralMechanism;
import frc.robot.subsystems.Elevator;
import static frc.robot.Enums.ElevatorEnums.*;
import static frc.robot.Enums.CoralEnums.*;

public class PrepareToScore extends SequentialCommandGroup{

    Elevator _elevator;
    CoralMechanism _coralMechanism;
    ElevatorPosition _elevatorLevel;
    CoralMechanismAngle _coralAngle;


    public PrepareToScore(ElevatorPosition elevatorSetpoint){
        _elevator = Elevator.instance;
        _coralMechanism = CoralMechanism.instance;
    }


    //TODO this one needs some WORK
    //TODO move "sadCoral" to front of sequence?
    public PrepareToScore(Elevator elevator, CoralMechanism coralMechanism, ElevatorPosition targetPosition){
        this.addCommands(
            new ParallelCommandGroup(
            new SetElevator(elevator, targetPosition, SetElevatorCancelBehavior.CANCEL_SETPOINT_REACHED),
            new SetCoralAngle(CoralMechanism.getAngleFromElevatorSetpoint(targetPosition), MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED) 
        ),
        new PrintCommand("Elevator set to " + targetPosition)
        );
    }
}
