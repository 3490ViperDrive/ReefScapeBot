package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WrapperCommand;
import frc.robot.commands.MoveCoralMechanism.CoralMechanismPosition;
import frc.robot.commands.MoveCoralMechanism.MoveCoralCancelBehavior;
import frc.robot.commands.RunCoralIntake.CoralIntakeDirection;
import frc.robot.subsystems.CoralMechanism;

/**
 * Moves the coral mechanism to the intake position, and runs the intake to suck coral in.
 * Returns the coral mechanism to the stow position once this command is canceled.
 * Cancels once coral is detected in the intake. Safe for autonomous use.
 */
public class CoralIntakeSequence extends WrapperCommand {

    public static final double CORAL_DETECTED_DELAY = 0.25; //seconds

    private final CoralMechanism coralMechanism;

    public CoralIntakeSequence(CoralMechanism coralMechanism) {
        super(
            new SequentialCommandGroup(
                new MoveCoralMechanism(coralMechanism,
                                       CoralMechanismPosition.INTAKE,
                                       MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED),
                new RunCoralIntake(coralMechanism, CoralIntakeDirection.IN)
                    .raceWith(new SequentialCommandGroup(Commands.waitUntil(() -> coralMechanism.getCoralDetected()),
                                                         Commands.waitSeconds(CORAL_DETECTED_DELAY)))));
        this.coralMechanism = coralMechanism;
        super.setName("Coral Intake Sequence");
    }

    //Coral intake must return to stow position whether the command group ends or is interrupted.
    //Could also be done with command.finallyDo(), but then this wrapper command
    //would wrap another wrapper command, which is silly.
    @Override
    public void end(boolean interrupted) {
        //this might be redundant, but when it wasn't there it didn't worky
        //and when it there it worky, so it stays
        coralMechanism.stopIntake();
        coralMechanism.setPivotSetpoint(CoralMechanismPosition.STOWED.getAngle());
    }
}
