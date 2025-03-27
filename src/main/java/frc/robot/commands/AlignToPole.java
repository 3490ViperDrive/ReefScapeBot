// Checks the angle/rotation and distance to the April Tag
// aligns drivetrain to left or right pole of the reef
package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.Drivetrain;
import frc.robot.Enums;
import frc.robot.Enums.ReefEnums;

public class AlignToPole extends Command {
    Vision _vision;
    Drivetrain _drivetrain;
    
    private double targetRange;
    private double targetYaw;
    private double targetAngle;
    private double turn;

    private double tolerance = 0.0;
    private double turnrate = 1.75;
    private double visionKp = 5;
    private SwerveRequest.RobotCentric robotCentric = new SwerveRequest.RobotCentric();


public AlignToPole(ReefEnums aPole) {
    _vision = Vision.instance;
    _drivetrain = Drivetrain.instance;
    addRequirements(_vision, _drivetrain);
}

@Override
public void execute(){
    if(_vision.getTargetStatus()){
        targetYaw = _vision.getYaw();

        SmartDashboard.putNumber("Angle", targetAngle);
        if (targetYaw > tolerance){
            turn = -1.0 * (0 - targetYaw) * visionKp * turnrate;
        } else if (targetYaw < tolerance) {
            turn = (0 - targetYaw) * visionKp * turnrate;
        }
        // if (aPole == ReefEnums.LEFT_POLE){
        //     xDist = -1.0 * LEFT_POLE.getReefPoleOffset();
        // } else {
        //     xDist = RIGHT_POLE.getReefPoleOffset();
        // }

        //TODO: need to strafe
        robotCentric.withVelocityX(0).
                        withVelocityY(0).
                        withRotationalRate(turn);

    }
    // CommandScheduler.getInstance().schedule(new Drive(_drivetrain, ))

    // _drivetrain.applySwerveRequest(robotCentric);
}

@Override
public boolean isFinished() {
    if(targetAngle <= tolerance) {
        return true;
    }
    return false;
}

}