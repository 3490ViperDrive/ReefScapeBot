package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.SetElevator.SetElevatorCancelBehavior;
import frc.robot.subsystems.CoralMechanism;
import frc.robot.subsystems.Elevator;
import static frc.robot.Enums.ElevatorEnums.*;
import static frc.robot.Enums.CoralEnums.*;

public class PrepareToScore extends SequentialCommandGroup{

    Elevator _elevator;
    CoralMechanism _coralMechanism;
    TargetLevel _logicalPosition;
    ElevatorPosition _realPosition; //TODO the naming conventions here are a bit painful.


    public PrepareToScore(TargetLevel elevatorTarget, ElevatorPosition elevatorSetpoint){
        _elevator = Elevator.instance;
        _coralMechanism = CoralMechanism.instance;
    }


    //TODO this one needs some WORK
    public PrepareToScore(Elevator elevator, CoralMechanism coralMechanism, TargetLevel logicalPosition, ElevatorPosition realPosition){
        //TODO why don't we move the "set the coral mechanism to look downward" bit to execute before the rest of the stuff does, to prevent
        //TODO any accidental breakage of the coral mechanism? seems an arbitrary rule to set
        this.addCommands(
            new ParallelCommandGroup(
            new InstantCommand(() -> elevator.setTargetLevel(logicalPosition)),
            new SetElevator(elevator, realPosition, SetElevatorCancelBehavior.CANCEL_SETPOINT_REACHED),
            new SetCoralAngle(coralMechanism, () -> ScoreCoralSequence.mapLogicalToCoral(logicalPosition), MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED) 
        ),
        new PrintCommand("Elevator set to " + logicalPosition)
        );
    }
}
