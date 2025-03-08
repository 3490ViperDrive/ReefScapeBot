package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

/** Adam note: Whoever wrote this command, see me after class. */
public class bruh extends Command {
    Drivetrain Drivetrain;
        private static SwerveRequest.FieldCentric fieldCentricRequest;


    public bruh(Drivetrain drivetrain) {
        this.Drivetrain = drivetrain;
                fieldCentricRequest = new SwerveRequest.FieldCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);

        addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        super.execute();Drivetrain.applySwerveRequest(
        fieldCentricRequest.withVelocityX(-1)
        .withVelocityY(0)
        .withRotationalRate(0));
    }


    @Override
    public boolean isFinished() {
        return DriverStation.isTeleop();
    }
    
}
