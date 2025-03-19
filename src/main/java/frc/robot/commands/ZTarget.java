package frc.robot.commands;


import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

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
        
    }


}
