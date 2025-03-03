package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WrapperCommand;
import frc.robot.commands.MoveCoralMechanism.CoralMechanismPosition;
import frc.robot.commands.MoveCoralMechanism.MoveCoralCancelBehavior;
import frc.robot.commands.RunCoralIntake.CoralIntakeDirection;
import frc.robot.subsystems.CoralMechanism;

/**
 * Moves the coral mechanism to the given scoring position, and runs the intake to spit the coral out.
 * Returns the coral mechanism to the stow position when this command is canceled.
 * If using this command in autonomous, you'll want to add a timeout or other condition to cancel it;
 * this command will not cancel itself.
 */
public class CoralScoreSequence extends WrapperCommand {
    private final CoralMechanism coralMechanism;

    public CoralScoreSequence(CoralMechanism coralMechanism, CoralMechanismPosition scoringPosition) {
        super(
            new SequentialCommandGroup(
                new MoveCoralMechanism(coralMechanism,
                                       scoringPosition,
                                       MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED),
                new RunCoralIntake(coralMechanism, CoralIntakeDirection.OUT)
            )
        );
        //check if the scoring position makes sense, but don't crash if it doesn't
        switch (scoringPosition) {
            case INTAKE:
            case STOWED:
                DriverStation.reportWarning(scoringPosition +
                                            " is a weird angle for CoralScoreSequence",
                                            false);
                break;
            default:
                break;
        }
        this.coralMechanism = coralMechanism;
        super.setName("Coral Score Sequence");
    }

    //Coral intake must return to stow position whether the command group ends or is interrupted.
    //Could also be done with command.finallyDo(), but then this wrapper command
    //would wrap another wrapper command, which is silly.
    @Override
    public void end(boolean interrupted) {
        coralMechanism.setPivotSetpoint(CoralMechanismPosition.STOWED.getAngle());
    }
}
