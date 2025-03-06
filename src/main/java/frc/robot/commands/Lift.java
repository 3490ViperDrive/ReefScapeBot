package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climba;

public class Lift extends Command{
    Climba theSolenoid;
    int whichDirection;

    

    public Lift(Climba thisSolenoid, int whatdirection){
        theSolenoid = thisSolenoid;
        whichDirection = whatdirection;
        addRequirements(theSolenoid);
    }
    
    public void execute(){
        theSolenoid.triggerSolenoid(whichDirection);
    }
}
