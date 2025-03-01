package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralMechanism;

/**
 * Runs the coral intake.
 * If CoralIntakeDirection == IN, cancels once coral is detected in the intake.
 */
public class RunCoralIntake extends Command {
    public static final double INTAKE_SPEED = 12; //volts

    private final CoralMechanism coralMechanism;
    private final CoralIntakeDirection direction;

    public enum CoralIntakeDirection {
        IN,
        OUT
    }

    public RunCoralIntake(CoralMechanism coralMechanism, CoralIntakeDirection direction) {
        this.coralMechanism = coralMechanism;
        this.direction = direction;
        super.addRequirements(coralMechanism);
        super.setName("Run Coral Intake " + direction);
    }

    @Override
    public void initialize() {
        if (direction == CoralIntakeDirection.OUT) {
            coralMechanism.runIntake(-INTAKE_SPEED);
        } else {
            coralMechanism.runIntake(INTAKE_SPEED);
        }
    }

    @Override
    public void end(boolean cancelled) {
        coralMechanism.stopIntake();
    }

    @Override
    public boolean isFinished() {
        if (direction == CoralIntakeDirection.OUT) {
            return false;
        } else {
            return coralMechanism.getCoralDetected();
        }
    }
}
