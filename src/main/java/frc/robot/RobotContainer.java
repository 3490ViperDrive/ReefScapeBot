// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DriveOpenLoop;
import frc.robot.commands.ZeroYaw;
import frc.robot.subsystems.*;
import frc.robot.utils.GamepadFilter;

@Logged
public class RobotContainer {

  public static final int DRIVER_CONTROLLER_PORT = 0;
  public static final double DRIVER_CONTROLLER_DEADBAND = 0.1;

  private final Drivetrain drivetrain;
  private final CommandXboxController gamepad;
  private final GamepadFilter gamepadFilter;

  public RobotContainer() {
    drivetrain = new Drivetrain();
    //these controls are temporary, todo decide omnicontrol implementation if any
    gamepad = new CommandXboxController(DRIVER_CONTROLLER_PORT);
    gamepadFilter = new GamepadFilter(gamepad, DRIVER_CONTROLLER_DEADBAND);

      drivetrain.setDefaultCommand(
        new DriveOpenLoop(
          drivetrain,
          gamepadFilter::getX,
          gamepadFilter::getY,
          gamepadFilter::getTheta,
          () -> gamepad.rightBumper().getAsBoolean()));

    //puts the zero yaw command as a button on the dashboard
    SmartDashboard.putData(new ZeroYaw(drivetrain));
    configureBindings();
  }

  private void configureBindings() {}

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
