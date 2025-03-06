// THIS IS NOT PERMANENT. 
// THIS EXIST JUST SO I CAN PROVE TO SOME PEOPLE THAT ALGAEMECHANISM WORKS MANUALLY. 
// THIS IS A TEST FILE AND WILL GET COMMENTED OUT

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeMechanism;

public class ManualPivotAlgae extends Command{
    AlgaeMechanism algaeMechanism;
    double voltage;

    public ManualPivotAlgae(AlgaeMechanism algaeMechanism, double voltage) {
        this.algaeMechanism = algaeMechanism;
        this.voltage = voltage;
        addRequirements(algaeMechanism);
    }

    @Override
    public void initialize() {
        algaeMechanism.runAlgaePivot(voltage);
    }

    @Override
    public void end(boolean interrupted) {
        algaeMechanism.stopAlgaePivot();
    }
}
