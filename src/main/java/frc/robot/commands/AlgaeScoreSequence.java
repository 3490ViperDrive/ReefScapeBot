package frc.robot.commands;

//import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
//import frc.robot.commands.SetElevator.SetElevatorCancelBehavior;
import frc.robot.subsystems.AlgaeMechanism;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorPosition;

public class AlgaeScoreSequence extends SequentialCommandGroup {
    public AlgaeScoreSequence(AlgaeMechanism algaeMechanism, Elevator elevator, ElevatorPosition cryingPosition) {
        addCommands(
            new SetElevator(elevator, () -> cryingPosition, SetElevator.SetElevatorCancelBehavior.CANCEL_SETPOINT_REACHED),
            //new InstantCommand(() -> algaeMechanism.setAlgaePivotSetpoint(AlgaeMechanism.HOLD_SCORE)),
            new ManualOuttakeAlgae(algaeMechanism)
        );
    }
}