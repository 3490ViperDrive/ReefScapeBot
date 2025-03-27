// package frc.robot.commands;

// import static frc.robot.Enums.CoralEnums.CoralIntakeDirection.IN;
// import static frc.robot.Enums.CoralEnums.CoralMechanismAngle.INTAKE;
// import static frc.robot.Enums.CoralEnums.MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED;

// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// public class EatCoralSequence extends SequentialCommandGroup{
    
// //     public EatCoralSequence(){
// //         this.addCommands(
// //             new ParallelCommandGroup(
// //                 new SetCoralAngle(INTAKE, CANCEL_SETPOINT_REACHED),
// //                 new RunCoralMotor(IN).raceWith(new SequentialCommandGroup(Commands.waitUntil(() -> coralMechanism.getCoralDetected()),
// //                 Commands.waitSeconds(CORAL_DETECTED_DELAY);
// //             )
// //         );
// //     }
// // }
