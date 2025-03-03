package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralMechanism;

/**
 * Moves the coral mechanism to an angle using closed-loop controls.
 */
public class MoveCoralMechanism extends Command {
    private final CoralMechanism coralMechanism;
    private final double setpoint;
    private final MoveCoralCancelBehavior cancelBehavior;

    public enum MoveCoralCancelBehavior {
        CANCEL_IMMEDIATELY,
        CANCEL_SETPOINT_REACHED
    }

    //todo find actual numbers for these
    public enum CoralMechanismPosition {
        STOWED(0.16),
        INTAKE(0.1),
        SCORE_L2(-0.05),
        SCORE_L3(-0.05),
        SCORE_L4(-0.138);

        private final double angle;

        CoralMechanismPosition(double angle) {
            this.angle = angle;
        }

        public double getAngle() {
            return angle;
        }
    }

    public MoveCoralMechanism(CoralMechanism coralMechanism,
                              double setpoint,
                              MoveCoralCancelBehavior cancelBehavior) {
        this.coralMechanism = coralMechanism;
        this.setpoint = setpoint;
        this.cancelBehavior = cancelBehavior;
        super.addRequirements(coralMechanism);
        super.setName(String.format("Move Coral Mech to %5f", setpoint));
    }

    public MoveCoralMechanism(CoralMechanism coralMechanism,
                              CoralMechanismPosition setpoint,
                              MoveCoralCancelBehavior cancelBehavior) {
        this(coralMechanism, setpoint.getAngle(), cancelBehavior);
        super.setName("Move Coral Mech to " + setpoint);
    }

    @Override
    public void initialize() {
        coralMechanism.setPivotSetpoint(setpoint);
    }

    @Override
    public boolean isFinished() {
        if (cancelBehavior == MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED) {
            return coralMechanism.getAtSetpoint();
        } else {
            return true;
        }
    }
}
