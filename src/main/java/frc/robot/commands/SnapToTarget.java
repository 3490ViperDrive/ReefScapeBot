package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.*;

public class SnapToTarget extends Command{

    private double targetYaw;
    private double vTurn;
    
    Vision theTagCamera;
    Drivetrain theDrivetrain;
    Drive theDriveCmd;

    public SnapToTarget(Vision aVision, Drivetrain aDrivetrain, Drive aDrive){
        theTagCamera = aVision;
        theDrivetrain = aDrivetrain;
        theDriveCmd = aDrive;
        addRequirements(theTagCamera, theDrivetrain);
    }

    @Override
    public void execute(){
        if(theTagCamera.getTargetStatus()){
            targetYaw = theTagCamera.getYaw();
            vTurn = -1.0 * targetYaw * theTagCamera.getVisionKp();
        } else {
            targetYaw = 0;
        }
        new Drive(theDrivetrain, () -> 0, () -> 0, () -> vTurn, () -> true);
    }
}
