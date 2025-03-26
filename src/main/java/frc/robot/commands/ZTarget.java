package frc.robot.commands;


import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Vision;


/**
 * A modified version of the "SnapToTarget" command that instead sends a SwerveRequest directly
 * to the drivetrain. MAY end up being of use for AprilTag-related positioning, though "SnapToTarget"
 * should do the trick just fine for now.
 */
public class ZTarget extends Command{
    
    Vision _vision;
    Drivetrain _drivetrain;
    private static final SwerveRequest.RobotCentric robotCentricRequest;

    private double targetYaw;

    /**
     * Describes the range of values at which the apriltag is considered "on target"
     */
    private double tolerance = 0.25;
    private double turnrate = 1.75;
    private SwerveRequest.RobotCentric robotCentric = new SwerveRequest.RobotCentric();   



    static {
        robotCentricRequest = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
    }
    
    public ZTarget(){
        _vision = Vision.instance;
        _drivetrain = Drivetrain.instance;
        addRequirements(_vision, _drivetrain);
    }

    @Override
    public void execute(){

        if(_vision.getTargetStatus()){
            targetYaw = _vision.getYaw();
            SmartDashboard.putNumber("YAW", targetYaw);
            if(targetYaw > tolerance){
                robotCentric.withVelocityX(0).
                                    withVelocityY(0).
                                    withRotationalRate(-turnrate);

            } else if(targetYaw < tolerance){
                robotCentric.withVelocityX(0).
                        withVelocityY(0).
                    withRotationalRate(turnrate);
            }
        }

        //TODO 1 rad/s ~= 60 deg/s
        _drivetrain.applySwerveRequest(robotCentric);
    }

    private double smoothingFunction(double input){
        return input;
    }

    @Override
    public boolean isFinished(){
        if(targetYaw <= tolerance ){
            return true;
        }
        return false;

    }


}
