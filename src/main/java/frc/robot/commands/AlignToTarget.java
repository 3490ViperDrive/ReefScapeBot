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

    private double targetYaw;
    private double tolerance = 0.03;
    private double strafeRate = 2.0;
    private double rotationRate = 1.0;
    private SwerveRequest.RobotCentric robotCentric = new SwerveRequest.RobotCentric();

    public AlignToTarget(Drivetrain _drivetrain, BranchChoice branch){
        _rightCamera = RightCamera.instance;
        _leftcamera = LeftCamera.instance;
        _drivetrain = Drivetrain.instance;
        _branch = branch;
        addRequirements(_rightCamera, _leftcamera, _drivetrain);
    }

    @Override
    public void execute() {
        double currentYaw;
        if (_branch == BranchChoice.LEFT) {
            targetYaw = 0;
            currentYaw = _rightCamera.getYaw();
            double currentSkew = _rightCamera.getSkew();

            if (currentSkew < 0 ) {
                robotCentric.withVelocityX(0).
                                withVelocityY(0).
                                withRotationalRate(-rotationRate);
            }
            if (currentSkew > 0 ) {
                robotCentric.withVelocityX(0).
                                withVelocityY(0).
                                withRotationalRate(rotationRate); 
            }
                //TODO: These might be backwards
            if (currentYaw > tolerance) {
                robotCentric.withVelocityX(-strafeRate).
                                    withVelocityY(0).
                                    withRotationalRate(0);
                }
            if (currentYaw < tolerance) {
                robotCentric.withVelocityX(strafeRate).
                                    withVelocityY(0).
                                    withRotationalRate(0);
                }
        }
        if (_branch == BranchChoice.RIGHT) {
            targetYaw = 0;
            currentYaw = _leftcamera.getYaw();
            //TODO: These might be backwards
            if (currentYaw > tolerance) {
                robotCentric.withVelocityX(strafeRate).
                                withVelocityY(0).
                                withRotationalRate(0);
            }
            if (currentYaw < tolerance) {
                robotCentric.withVelocityX(-strafeRate).
                                withVelocityY(0).
                                withRotationalRate(0);
            }
        }
    }
}
