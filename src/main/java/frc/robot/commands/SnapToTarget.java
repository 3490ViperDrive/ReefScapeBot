package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.*;

public class SnapToTarget extends Command{

    private double targetYaw;
    private double vTurn = 0;
    double xVal = 0;
    double yVal = 0;
    
    Vision theTagCamera;
    Drivetrain theDrivetrain;
    Drive theDriveCmd;
    SwerveRequest.RobotCentric turnRequest;


    public SnapToTarget(Vision aVision, Drivetrain aDrivetrain){
        setName("Snapping to target!");
        theTagCamera = aVision;
        theDrivetrain = aDrivetrain;
        //theDriveCmd = aDrive;
        addRequirements(theTagCamera, theDrivetrain);
    }

    @Override
    public void execute(){
        if(theTagCamera.getTargetStatus()){
            targetYaw = theTagCamera.getYaw();
            SmartDashboard.putNumber("Yaw", theTagCamera.getYaw());
            SmartDashboard.putNumber("TagKP", theTagCamera.getVisionKp());
            vTurn =  targetYaw * theTagCamera.getVisionKp();
            SmartDashboard.putNumber("vTurn", vTurn);
        } else {
            targetYaw = 0;
        }

        turnRequest = new SwerveRequest.RobotCentric().withVelocityX(0).withVelocityY(0).withRotationalRate(vTurn);
        theDrivetrain.applySwerveRequest(turnRequest);
    }

    

}
