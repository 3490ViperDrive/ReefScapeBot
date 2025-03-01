package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class Lift extends Command{
    Climber theSolenoid;
    int whichDirection;

    

    public Lift(Climber thisSolenoid, int whatdirection){
        theSolenoid = thisSolenoid;
        whichDirection = whatdirection;
        
    }
    
    public void execute(){
        theSolenoid.triggerSolenoid(whichDirection);
    }
}
