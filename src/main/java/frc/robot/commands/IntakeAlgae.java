package frc.robot.commands;

import frc.robot.subsystems.AlgaeMechanism;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeAlgae extends Command {
    double ALGAE_INTAKE_SPEED = 12;

    AlgaeMechanism algaeMechanism;
    AlgaeIntakeDirection algaeDirection;

    enum AlgaeIntakeDirection {
        ALGAE_IN, ALGAE_OUT
    }

    public IntakeAlgae(AlgaeMechanism algaeMechanism, AlgaeIntakeDirection algaeDirection) {
        this.algaeMechanism = algaeMechanism;
        this.algaeDirection = algaeDirection;
        super.addRequirements(algaeMechanism);
        super.setName("Run Algae Intake" + algaeDirection);
    }

    @Override
    public void execute() {
        if (algaeDirection == AlgaeIntakeDirection.ALGAE_IN) {
            algaeMechanism.runAlgaeIntake(ALGAE_INTAKE_SPEED);
        } else {
            algaeMechanism.runAlgaeIntake(-ALGAE_INTAKE_SPEED);
        }
    }

    @Override
    public void end(boolean cancelled) {
        algaeMechanism.stopAlgaeIntake();
    }

}
