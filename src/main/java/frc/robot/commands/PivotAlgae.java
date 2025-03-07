package frc.robot.commands;

import frc.robot.subsystems.AlgaeMechanism;
import frc.robot.Enums.AlgaeMechanismPosition;

import edu.wpi.first.wpilibj2.command.Command;

public class PivotAlgae extends Command {
    AlgaeMechanism algaeMechanism;
    double algaeSetpoint;
    PivotAlgaeCancelBehavior algaeCancelBehavior;

    enum PivotAlgaeCancelBehavior {
        CANCEL, CANCEL_SETPOINT_COMPLETED
    }

    // use real numbers if interested


    public PivotAlgae(AlgaeMechanism algaeMechanism, double algaeSetpoint,
    PivotAlgaeCancelBehavior algaeCancelBehavior) {

        this.algaeMechanism = algaeMechanism;
        this.algaeSetpoint = algaeSetpoint;
        this.algaeCancelBehavior = algaeCancelBehavior;
        super.addRequirements(algaeMechanism);
        super.setName(String.format("Move Algae Mechanism to %5f", algaeSetpoint));
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
}
