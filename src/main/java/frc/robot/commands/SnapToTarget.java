package frc.robot.commands;

//import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.*;

public class SnapToTarget extends Command{

    private double targetYaw;
    private double vTurn;
    
    Vision theTagCamera;
    Drivetrain theDrivetrain;
    //Drive theDriveCmd;
    //SwerveRequest.RobotCentric turnRequest;

    public SnapToTarget(Vision aVision, Drivetrain aDrivetrain){
        theTagCamera = aVision;
        theDrivetrain = aDrivetrain;
        addRequirements(theTagCamera, theDrivetrain);
    }

    @Override
    public void execute(){
        if(theTagCamera.getTargetStatus()){
            targetYaw = theTagCamera.getYaw();
            vTurn = -1.0 * targetYaw * theTagCamera.getVisionKp();

            //TODO for debugging purposes; will be removed in final Shuffleboard layout.
            SmartDashboard.putNumber("Yaw", theTagCamera.getYaw());
            SmartDashboard.putNumber("TagKP", theTagCamera.getVisionKp());
            SmartDashboard.putNumber("vTurn", vTurn);
        } else {
            targetYaw = 0;
        }
        //I hate WPILib
        CommandScheduler.getInstance().schedule(new Drive(theDrivetrain, ()-> 0, ()-> 0, ()-> vTurn, ()-> true));
    }

    

}
