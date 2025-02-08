package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveRequest;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

/**
 * Bog standard teleop driving command. Takes in normalized velocities
 * (i.e. [-1, 1] where each extreme is full speed forwards or backwards)
 * and commands the Drivetrain to do the thing.
 */
public class DriveOpenLoop extends Command {
    private final Drivetrain drivetrain;

    //this might be bad?
    private static final SwerveRequest.RobotCentric robotCentricRequest;
    private static final SwerveRequest.FieldCentric fieldCentricRequest;
    private static final double maxTranslationMps;
    private static final double maxRotationRps;

    private final DoubleSupplier xSup;
    private final DoubleSupplier ySup;
    private final DoubleSupplier thetaSup;
    private final BooleanSupplier robotCentricSup;

    static {
        robotCentricRequest = new SwerveRequest.RobotCentric()
                                    .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
        fieldCentricRequest = new SwerveRequest.FieldCentric()
                                    .withDriveRequestType(DriveRequestType.OpenLoopVoltage);
        maxTranslationMps = Drivetrain.MAX_TRANSLATION_SPEED.in(Units.MetersPerSecond);
        maxRotationRps =  Drivetrain.MAX_ROTATION_SPEED.in(Units.RadiansPerSecond);
    }

    /**
     * Create a new DriveOpenLoop command. 
     * @param drivetrain the Drivetrain subsystem, declared in RobotContainer.
     * @param xSup forwards/backwards speed. [-1, 1]
     * @param ySup left/right speed. [-1, 1]
     * @param thetaSup rotational rate. [-1, 1]
     * @param robotCentricSup true to drive robot-centric, false to drive field-centric
     */
    public DriveOpenLoop(Drivetrain drivetrain,
                         DoubleSupplier xSup,
                         DoubleSupplier ySup,
                         DoubleSupplier thetaSup,
                         BooleanSupplier robotCentricSup) {
        this.drivetrain = drivetrain;
        this.xSup = xSup;
        this.ySup = ySup;
        this.thetaSup = thetaSup;
        this.robotCentricSup = robotCentricSup;
        super.addRequirements(drivetrain);
    }

    @Override
    public void execute() {
        if (robotCentricSup.getAsBoolean()) {
            drivetrain.applySwerveRequest(
                robotCentricRequest.withVelocityX(xSup.getAsDouble() * maxTranslationMps)
                                   .withVelocityY(ySup.getAsDouble() * maxTranslationMps)
                                   .withRotationalRate(thetaSup.getAsDouble() * maxRotationRps));
        } else {
            drivetrain.applySwerveRequest(
                fieldCentricRequest.withVelocityX(xSup.getAsDouble() * maxTranslationMps)
                                   .withVelocityY(ySup.getAsDouble() * maxTranslationMps)
                                   .withRotationalRate(thetaSup.getAsDouble() * maxRotationRps));
        }
    }

    @Override
    public void end(boolean interrupted) {
        fieldCentricRequest.withVelocityX(0)
                           .withVelocityY(0)
                           .withRotationalRate(0);
        robotCentricRequest.withVelocityX(0)
                           .withVelocityY(0)
                           .withRotationalRate(0);
    }
}
