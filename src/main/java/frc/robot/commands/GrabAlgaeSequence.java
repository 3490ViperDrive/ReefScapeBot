// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import frc.robot.commands.SetElevator.SetElevatorCancelBehavior;
// import frc.robot.subsystems.AlgaeMechanism;
// import frc.robot.subsystems.Elevator;
// import static frc.robot.Enums.ElevatorEnums.*;

// public class GrabAlgaeSequence extends SequentialCommandGroup {
//     public GrabAlgaeSequence(AlgaeMechanism algaeMechanism, Elevator elevator, ElevatorPosition badPosition) {
//         addCommands(
//             new SetElevator(elevator, () -> badPosition, SetElevatorCancelBehavior.CANCEL_SETPOINT_REACHED),
//             new InstantCommand(() -> algaeMechanism.setAlgaePivotSetpoint(AlgaeMechanism.HOLD_SCORE)),
//             new ManualIntakeAlgae(algaeMechanism)
//         );
//     }
// }