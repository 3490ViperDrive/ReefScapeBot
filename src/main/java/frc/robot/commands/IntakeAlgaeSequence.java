package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.IntakeAlgae.AlgaeIntakeDirection;
import frc.robot.subsystems.AlgaeMechanism;
import frc.robot.subsystems.Elevator;

public class IntakeAlgaeSequence extends SequentialCommandGroup{

    AlgaeMechanism algaeMechanism;
    Elevator elevator;

    public IntakeAlgaeSequence(Elevator elevator, AlgaeMechanism algaeMechanism, Elevator.Positions positions) {
        addCommands(
        new SetElevator(positions, elevator),
        new PivotAlgae(algaeMechanism, PivotAlgae.AlgaeMechanismPosition.GROUND, PivotAlgae.PivotAlgaeCancelBehavior.CANCEL_SETPOINT_COMPLETED),
        new IntakeAlgae(algaeMechanism, AlgaeIntakeDirection.ALGAE_IN).withTimeout(0.5)
        .until(algaeMechanism::checkIntakeCurrent),
        new SetElevator(Elevator.Positions.DEFAULT, elevator)
        );
        
        this.algaeMechanism = algaeMechanism;
        this.elevator = elevator;
    }
    
}
