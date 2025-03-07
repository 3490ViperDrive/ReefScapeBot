// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.AlgaeMechanism;

// public class ManualPivotDown extends Command{
//     private final AlgaeMechanism algaeMechanism;


//     public ManualPivotDown (AlgaeMechanism algaeMechanism) {
//         this.algaeMechanism = algaeMechanism;
//         super.addRequirements(algaeMechanism);
//     }

//     @Override
//     public void initialize() {
//             algaeMechanism.runAlgaePivot(-12);
//     }

//     @Override
//     public void end(boolean interrupted) {
//         algaeMechanism.stopAlgaePivot();
//     }
// }
