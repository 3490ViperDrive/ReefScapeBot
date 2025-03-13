package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class OperateClimber extends Command{
    Climber theSolenoid;
    int whichDirection;

    public OperateClimber(Climber thisSolenoid, int whatdirection){
        theSolenoid = thisSolenoid;
        whichDirection = whatdirection;
        addRequirements(theSolenoid);
    }
    
    public void execute(){
        theSolenoid.triggerSolenoid(whichDirection);
    }
}
