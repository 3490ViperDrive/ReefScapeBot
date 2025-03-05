package frc.robot.commands;

import frc.robot.subsystems.AlgaeMechanism;

import edu.wpi.first.wpilibj2.command.Command;

public class PivotAlgae extends Command {
    AlgaeMechanism algaeMechanism;
    double algaeSetpoint;
    PivotAlgaeCancelBehavior algaeCancelBehavior;

    enum PivotAlgaeCancelBehavior {
        CANCEL, CANCEL_SETPOINT_COMPLETED
    }

    // use real numbers if interested
    enum AlgaeMechanismPosition {
        INITIAL(0),
        GROUND(0),
        HOLD_SCORE(0);

        double algaeAngle;

        AlgaeMechanismPosition(double algaeAngle) {
            this.algaeAngle = algaeAngle;
        }

        double getAlgaeAngle() {
            return algaeAngle;
        }
    }

    public PivotAlgae(AlgaeMechanism algaeMechanism, double algaeSetpoint,
    PivotAlgaeCancelBehavior algaeCancelBehavior) {

        this.algaeMechanism = algaeMechanism;
        this.algaeSetpoint = algaeSetpoint;
        this.algaeCancelBehavior = algaeCancelBehavior;
        super.addRequirements(algaeMechanism);
        super.setName(String.format("Move Algae Mechanism to %.3f", algaeSetpoint));
    }

    public PivotAlgae(AlgaeMechanism algaeMechanism, AlgaeMechanismPosition algaeSetpoint,
    PivotAlgaeCancelBehavior algaeCancelBehavior) {
        this(algaeMechanism, algaeSetpoint.getAlgaeAngle(), algaeCancelBehavior);
        super.setName("Move Algae Mechanism to " + algaeSetpoint);
    }

    @Override
    public void execute() {
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
