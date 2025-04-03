package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralMechanism;
import frc.robot.Enums.CoralEnums.*;

/**
 * Runs the coral intake at a set speed.
 */
public class RunCoralMotor extends Command {
    public static final double INTAKE_SPEED = 10; //volts

    private final CoralMechanism coralMechanism;
    private final CoralIntakeDirection direction;


    public RunCoralMotor(CoralIntakeDirection direction) {
        this.coralMechanism = CoralMechanism.instance;
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
        /*if (direction == CoralIntakeDirection.OUT) {
            return false;
        } else {
            return coralMechanism.getCoralDetected();
        }*/
        return false;
    }
}
