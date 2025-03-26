package frc.robot.subsystems.vision;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile.Constraints;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.utils.CowboyUtils;

public class AlignWithPose extends Command {
    Drivetrain drive;
    Pose2d pose;
    ProfiledPIDController xController;
    ProfiledPIDController yController;
    ProfiledPIDController rotController;

    public AlignWithPose(Drivetrain drive, Pose2d pose) {
        this.drive = drive;
        this.pose = pose;
        addRequirements(drive);

        xController = new ProfiledPIDController(0.35, 0, 0, new Constraints(4, 3));
        yController = new ProfiledPIDController(0.35, 0, 0, new Constraints(4, 3));
        rotController = new ProfiledPIDController(0.031, 0, 0, new Constraints(50, 40));

        xController.setTolerance(.001);
        yController.setTolerance(.001);
        rotController.setTolerance(Units.degreesToRadians(.5));
        rotController.enableContinuousInput(-180, 180);
    }

    @Override
    public void end(boolean interrupted) {
        drive.drive(0, 0, 0, true, true);
    }

    @Override
    public void execute() {
        double xSpeed = MathUtil.clamp((xController.calculate(drive.getPose().getX(), pose.getX())), -1,
                1);
        double ySpeed = MathUtil.clamp((yController.calculate(drive.getPose().getY(), pose.getY())), -1,
                1);
        double rotSpeed = MathUtil.clamp(
                (rotController.calculate(drive.getPose().getRotation().getDegrees(),
                        pose.getRotation().getDegrees())),
                -1,
                1);

        if (CowboyUtils.isRedAlliance()) {
            drive.drive(-xSpeed, -ySpeed, rotSpeed, true, true);
        } else {
            drive.drive(xSpeed, ySpeed, rotSpeed, true, true);
        }

    }

    @Override
    public void initialize() {
        // Reset each controller using the current sensor readings
        xController.reset(drive.getPose().getX());
        yController.reset(drive.getPose().getY());
        rotController.reset(drive.getPose().getRotation().getDegrees());

        // Optionally, stop the drive before starting
        drive.drive(0, 0, 0, true, true);
    }

    @Override
    public boolean isFinished() {
        if (xController.atGoal() && yController.atGoal() && rotController.atGoal()) {
            return true;
        } else {
            return false;
        }
        // return false;
    }

}