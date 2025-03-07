// THIS IS NOT PERMANENT. 
// THIS EXIST JUST SO I CAN PROVE TO SOME PEOPLE THAT ALGAEMECHANISM WORKS MANUALLY. 
// THIS IS A TEST FILE AND WILL GET COMMENTED OUT

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeMechanism;

public class ManualIntakeAlgae extends Command{
    AlgaeMechanism algaeMechanism;

    public ManualIntakeAlgae(AlgaeMechanism algaeMechanism) {
        this.algaeMechanism = algaeMechanism;
        addRequirements(algaeMechanism);
    }

    @Override
    public void initialize(){
        algaeMechanism.runAlgaeIntake(12);
    }

    @Override
    public void end(boolean interrupted) {
        algaeMechanism.stopAlgaeIntake();
    }
    
}
