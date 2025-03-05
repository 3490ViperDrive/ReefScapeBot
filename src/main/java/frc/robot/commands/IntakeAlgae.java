package frc.robot.commands;

import frc.robot.subsystems.AlgaeMechanism;

import edu.wpi.first.wpilibj2.command.Command;

public class IntakeAlgae extends Command {
    double ALGAE_INTAKE_SPEED = 12;

    AlgaeMechanism algaeMechanism;
    AlgaeIntakeDirection algaeDirection;

    public enum AlgaeIntakeDirection {
        ALGAE_IN, ALGAE_OUT
    }

    public IntakeAlgae(AlgaeMechanism algaeMechanism, AlgaeIntakeDirection algaeDirection) {
        this.algaeMechanism = algaeMechanism;
        this.algaeDirection = algaeDirection;
        super.addRequirements(algaeMechanism);
        super.setName("Run Algae Intake" + algaeDirection);
    }

    @Override
    public void initialize() {
        if (algaeDirection == AlgaeIntakeDirection.ALGAE_IN) {
            algaeMechanism.runAlgaeIntake(ALGAE_INTAKE_SPEED);
        } else {
            algaeMechanism.runAlgaeIntake(-ALGAE_INTAKE_SPEED);
        }
    }

    //When algae detected = true, cancel intake
    @Override
    public void end(boolean cancelled) {
        algaeMechanism.stopAlgaeIntake();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    

}
