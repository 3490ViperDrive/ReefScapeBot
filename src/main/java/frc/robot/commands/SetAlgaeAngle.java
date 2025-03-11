package frc.robot.commands;

import frc.robot.subsystems.AlgaeMechanism;

import edu.wpi.first.wpilibj2.command.Command;

public class SetAlgaeAngle extends Command {
    AlgaeMechanism algaeMechanism;
    double algaeSetpoint;
    PivotAlgaeCancelBehavior algaeCancelBehavior;

    enum PivotAlgaeCancelBehavior {
        CANCEL, CANCEL_SETPOINT_COMPLETED
    }

    enum AlgaeMechanismPosition {
        // Stowed. TODO CALL IT SOMEWHERE, PLEASE
        INITIAL(0.245),
        // Ground intake
        GROUND(1),
        // Pick up from reef and score processor (should be the same angle)
        HOLD_SCORE(0);

        double algaeAngle;

        AlgaeMechanismPosition(double algaeAngle) {
            this.algaeAngle = algaeAngle;
        }

        double getAlgaeAngle() {
            return algaeAngle;
        }
    }

    public SetAlgaeAngle(AlgaeMechanism algaeMechanism, double algaeSetpoint,
    PivotAlgaeCancelBehavior algaeCancelBehavior) {

        this.algaeMechanism = algaeMechanism;
        this.algaeSetpoint = algaeSetpoint;
        this.algaeCancelBehavior = algaeCancelBehavior;
        super.addRequirements(algaeMechanism);
        super.setName(String.format("Move Algae Mechanism to %.3f", algaeSetpoint));
        //String.format?? NEERRDS -Khiry
    }

    public SetAlgaeAngle(AlgaeMechanism algaeMechanism, AlgaeMechanismPosition algaeSetpoint,
    PivotAlgaeCancelBehavior algaeCancelBehavior) {
        this(algaeMechanism, algaeSetpoint.getAlgaeAngle(), algaeCancelBehavior);
        super.setName("Move Algae Mechanism to " + algaeSetpoint);
    }

    @Override
    public void initialize() {
      algaeMechanism.setAlgaePivotSetpoint(algaeSetpoint);
    }

    @Override
    public boolean isFinished() {
        if(algaeCancelBehavior == PivotAlgaeCancelBehavior.CANCEL_SETPOINT_COMPLETED) {
            return algaeMechanism.getAtAlgaeSetpoint();
        } else {
            return true;
        }
    }

    @Override
    public void end(boolean cancelled) {
        algaeMechanism.stopAlgaePivot();
    }
}
