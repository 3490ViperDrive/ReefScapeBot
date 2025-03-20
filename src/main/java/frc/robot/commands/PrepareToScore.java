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
    ElevatorPosition _realPosition;


    public PrepareToScore(TargetLevel whereAt){
        _elevator = Elevator.instance;
        _coralMechanism = CoralMechanism.instance;
        //this(_elevator, _coralMechanism, whereAt, )
    }


    //TODO this one needs some WORK
    public PrepareToScore(Elevator elevator, CoralMechanism coralMechanism, TargetLevel logicalPosition, ElevatorPosition realPosition){
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
