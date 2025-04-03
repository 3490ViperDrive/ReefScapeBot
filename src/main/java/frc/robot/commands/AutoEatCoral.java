package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralMechanism;

public class AutoEatCoral extends Command{
    
    CoralMechanism _coralMechanism;
    Timer commandTimer; 
    private double timeoutWindow = 0.25; //how many seconds to run the mechanism's intake for after detecting a coral
    private boolean timerHasStarted = false;

    //Testing to see if the stupidity arose from idiosyncrasies of the Timer class
    private double timeToStop;

    public AutoEatCoral(){
        //commandTimer = new Timer();
        _coralMechanism = CoralMechanism.instance;
        addRequirements(_coralMechanism);
    }

    @Override
    public void execute(){
        //only start the timer on the call where coral is first detected
        _coralMechanism.runIntake(10);
        if(_coralMechanism.getCoralDetected() == true && timerHasStarted == false){
            //commandTimer.start();
            timeToStop = Timer.getFPGATimestamp() + timeoutWindow;
            timerHasStarted = true;
        }
    }

    //TODO simpler implementation without built-in timer
    // public boolean isFinished(){
    //     if(_coralMechanism.getCoralDetected()){
    //         return true;
    //     }
    //     return false;
    // }

    @Override
    public boolean isFinished(){
       //if(commandTimer.hasElapsed(timeoutWindow)){
       if(Timer.getFPGATimestamp() > timeToStop){
            return true;
       }
       return false;
    }

    @Override
    public void end(boolean wasInterrupted){
        //Stop the intake motors
        _coralMechanism.stopIntake();
        // commandTimer.stop();
        // commandTimer.reset();
    }
}
