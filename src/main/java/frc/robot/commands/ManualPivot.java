// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.AlgaeMechanism;

// public class ManualPivot extends Command{
//     private final AlgaeMechanism algaeMechanism;
//     private final AlgaePivotDirection aDirection;

//     public enum AlgaePivotDirection {
//         DOWN,
//         UP
//       }

//     public ManualPivot(AlgaeMechanism algaeMechanism) {
//         this.algaeMechanism = algaeMechanism;
//         this.aDirection = aDirection;
//         super.addRequirements(algaeMechanism);
//     }

//     @Override
//     public void initialize() {
//         if (aDirection ==AlgaePivotDirection.DOWN) {
//             algaeMechanism.runAlgaePivot(-12);
//         } else { 
//             algaeMechanism.runAlgaeIntake(12);

//         }
//     }

//     @Override
//     public void end(boolean interrupted) {
//         algaeMechanism.stopAlgaePivot();
//     }
// }
