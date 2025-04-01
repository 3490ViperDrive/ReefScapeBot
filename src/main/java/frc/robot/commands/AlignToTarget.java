package frc.robot.commands;

import java.util.function.Supplier;

import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.LeftCamera;
import frc.robot.subsystems.RightCamera;
import frc.robot.Enums.BranchEnums.*;

public class AlignToTarget extends Command {
    //TODO: get results from both cameras
    //If left is passed into the command
    // strafe until right camera has full image of tag
    // do not move forward (Y axis) only in strafe (X axis)

    RightCamera _rightCamera;
    LeftCamera _leftcamera;
    Drivetrain _drivetrain;
    BranchChoice _branch;

    private double tolerance = 0.3;
    private double strafeRate = 2.0;
    private double rotationRate = 1.0;
    private SwerveRequest.RobotCentric robotCentric = new SwerveRequest.RobotCentric();

    public AlignToTarget(BranchChoice branch){
        _rightCamera = RightCamera.instance;
        _leftcamera = LeftCamera.instance;
        _drivetrain = Drivetrain.instance;
        _branch = branch;
        addRequirements(_rightCamera, _leftcamera, _drivetrain);
    }

    @Override
    public void execute() {
        if (_branch == BranchChoice.LEFT) {
            // focus on right camera
            if (_rightCamera.getTargetStatus()) {
                double currentYaw = _rightCamera.getYaw();
                if (currentYaw > tolerance) {
                    robotCentric.withVelocityX(0).
                                        withVelocityY(0).
                                        withRotationalRate(-rotationRate);
                } else if (currentYaw < tolerance) {
                    robotCentric.withVelocityX(0).
                                        withVelocityY(0).
                                        withRotationalRate(rotationRate);         
                } else {
                    robotCentric.withVelocityX(0).
                                    withVelocityY(0).
                                    withRotationalRate(0);  
                }
            }
            else {
                //we need to strafe to see it first
                robotCentric.withVelocityX(-strafeRate).
                                withVelocityY(0).
                                withRotationalRate(0);
            }
        }
        if (_branch == BranchChoice.RIGHT) {
            if (_leftcamera.getTargetStatus()) {
                double currentYaw = _rightCamera.getYaw();
                if (currentYaw > tolerance) {
                    robotCentric.withVelocityX(0).
                                        withVelocityY(0).
                                        withRotationalRate(-rotationRate);
                } else if (currentYaw < tolerance) {
                    robotCentric.withVelocityX(0).
                                        withVelocityY(0).
                                        withRotationalRate(rotationRate);         
                } else {
                    robotCentric.withVelocityX(0).
                                    withVelocityY(0).
                                    withRotationalRate(0);  
                }
            }
        else {
            //we need to strafe to see it first
            robotCentric.withVelocityX(strafeRate).
                            withVelocityY(0).
                            withRotationalRate(0);
            }
        }

        _drivetrain.applySwerveRequest(robotCentric);
    }
}
