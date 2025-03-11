package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WrapperCommand;
import frc.robot.commands.SetElevator.SetElevatorCancelBehavior;
import frc.robot.subsystems.CoralMechanism;
import frc.robot.subsystems.Elevator;
import frc.robot.Enums.ElevatorEnums.*;
import frc.robot.Enums.CoralEnums.*;

/**
 * Moves the coral mechanism to the given scoring position, and runs the intake to spit the coral out.
 * Returns the coral mechanism to the stow position when this command is canceled.
 * If using this command in autonomous, you'll want to add a timeout or other condition to cancel it;
 * this command will not cancel itself.
 */
public class ScoreCoralSequence extends WrapperCommand{
    private final CoralMechanism coralMechanism;

    public ScoreCoralSequence(CoralMechanism coralMechanism, Elevator elevator) {
        super(
            new SequentialCommandGroup(
                new ParallelCommandGroup(
                    new SetElevator(elevator,
                                    () -> mapLogicalToElevator(elevator.getCurrentTarget()),
                                    SetElevatorCancelBehavior.CANCEL_SETPOINT_REACHED),
                    new ChangeCoralAngle(coralMechanism,
                        () -> mapLogicalToCoral(elevator.getCurrentTarget()),
                        MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED)),
                new RunCoralIntake(coralMechanism, CoralIntakeDirection.OUT)
            )
        );
        this.coralMechanism = coralMechanism;
        super.setName("Coral Score Sequence");
    } 

    //Coral intake must return to stow position whether the command group ends or is interrupted.
    //Could also be done with command.finallyDo(), but then this wrapper command
    //would wrap another wrapper command, which is silly.
    @Override
    public void end(boolean interrupted) {
        coralMechanism.stopIntake();
        //coralMechanism.resetToStraight();
        //coralMechanism.setPivotSetpoint(CoralMechanismPosition.STOWED.getAngle());
    }

    public static ElevatorPosition mapLogicalToElevator(TargetLevel logicalPosition) {
        switch (logicalPosition) {
            case L1:
                return ElevatorPosition.CORAL_L1;
            case L3:
                return ElevatorPosition.CORAL_L3;
            case L4:
                return ElevatorPosition.CORAL_L4;
            case L2:
            default:
                return ElevatorPosition.CORAL_L2;
        }
    }

    public static CoralMechanismPosition mapLogicalToCoral(TargetLevel logicalPosition) {
        switch (logicalPosition) {
            case L1:
                return CoralMechanismPosition.SCORE_L1;
            case L3:
                return CoralMechanismPosition.SCORE_L3;
            case L4:
                return CoralMechanismPosition.SCORE_L4;
            case L2:
            default:
                return CoralMechanismPosition.SCORE_L2;
        }
    }
}
