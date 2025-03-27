package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralMechanism;

public class AutoEatCoral extends Command{
    
    CoralMechanism _coralMechanism;

    public AutoEatCoral(){
        _coralMechanism = CoralMechanism.instance;
        addRequirements(_coralMechanism);
    }

    @Override
    public void execute(){
        _coralMechanism.runIntake(10);
    }

    public boolean isFinished(){
        if(_coralMechanism.getCoralDetected()){
            return true;
        }
        return false;
    }
}
