package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.SetElevator.SetElevatorCancelBehavior;
import frc.robot.subsystems.AlgaeMechanism;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ElevatorPosition;

public class AlgaeIntakeSequence extends SequentialCommandGroup {
    public AlgaeIntakeSequence(AlgaeMechanism algaeMechanism, Elevator elevator, ElevatorPosition badPosition) {
        addCommands(
            new SetElevator(elevator, () -> badPosition, SetElevatorCancelBehavior.CANCEL_SETPOINT_REACHED),
            new InstantCommand(() -> algaeMechanism.setAlgaePivotSetpoint(AlgaeMechanism.HOLD_SCORE)),
            new ManualIntakeAlgae(algaeMechanism)
        );
    }
}