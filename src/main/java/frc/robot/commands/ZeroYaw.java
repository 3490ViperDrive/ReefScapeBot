package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Drivetrain;

public class ZeroYaw extends Command {
    private final Drivetrain drivetrain;

    public ZeroYaw(Drivetrain drivetrain) {
        this.drivetrain = drivetrain;
        super.setName("Zero Yaw Command");
    }

    @Override
    public void initialize() {
        drivetrain.zeroYaw();
    }

    @Override
    public boolean isFinished() {
        return true; //done once initialized
    }
}
