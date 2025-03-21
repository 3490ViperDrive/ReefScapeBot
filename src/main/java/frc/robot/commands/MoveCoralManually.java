package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralMechanism;

public class MoveCoralManually extends Command{

    CoralMechanism coralMechanism;
    double coralVolts;

    public MoveCoralManually(double pivotVolts) {
        coralMechanism = CoralMechanism.instance;
        coralVolts = pivotVolts;
        addRequirements(coralMechanism);
    }

    @Override
    public void execute() {
        coralMechanism.runPivotOpenLoop(coralVolts);
    }

    @Override
    public void end(boolean isCanceled){
        coralMechanism.stopPivot();
    }
}