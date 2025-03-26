package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Bandicams;

public class SwitchCamera extends Command{
    
    Bandicams hypercam;
    static boolean isClimbCamera = false;
    NetworkTableEntry cameraSelection;

    public SwitchCamera(){
        isClimbCamera = !isClimbCamera;
        hypercam = Bandicams.instance;
        cameraSelection = Robot.cameraSelection;
        addRequirements(hypercam);
    }

    @Override
    public void initialize(){
        //TODO mild spaghetti
        if(isClimbCamera){
            cameraSelection.setString(hypercam.getClimbCam().getName());
        } else {
            cameraSelection.setString(hypercam.getCoralCam().getName());
        }
    }
}
