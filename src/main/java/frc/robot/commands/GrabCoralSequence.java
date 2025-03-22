package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WrapperCommand;
import frc.robot.Enums.ElevatorEnums.SetElevatorCancelBehavior;
import frc.robot.subsystems.CoralMechanism;
import frc.robot.subsystems.Elevator;
import static frc.robot.Enums.ElevatorEnums.*;
import static frc.robot.Enums.CoralEnums.*;

/**
 * Moves the coral mechanism to the intake position, and runs the intake to suck coral in.
 * Returns the coral mechanism to the stow position once this command is canceled.
 * Cancels once coral is detected in the intake. Safe for autonomous use.
 */
public class GrabCoralSequence extends WrapperCommand {

    Elevator _elevator;
    CoralMechanism _coralMechanism; 

    public static final double CORAL_DETECTED_DELAY = 0.25; //seconds

    private final CoralMechanism coralMechanism;

    //TODO this indentation is SINFUL
    public GrabCoralSequence(CoralMechanism coralMechanism, Elevator elevator) {
        //TODO wouldn't a regular if statement also work?
        super(
            new SequentialCommandGroup(
                new ParallelCommandGroup(
                    new ConditionalCommand(
                        new SetElevator(elevator, ElevatorPosition.CORAL_INTAKE, SetElevatorCancelBehavior.CANCEL_SETPOINT_REACHED),
                        new InstantCommand(),
                        () -> elevator.getCurrentTarget() == TargetLevel.L1),
                    new SetCoralAngle(coralMechanism,
                                           CoralMechanismAngle.INTAKE,
                                           MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED)),
                new RunCoralMotor(coralMechanism, CoralIntakeDirection.IN)
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
        coralMechanism.resetToStraight();
        //coralMechanism.setPivotSetpoint(CoralMechanismPosition.STOWED.getAngle());
    }
}
