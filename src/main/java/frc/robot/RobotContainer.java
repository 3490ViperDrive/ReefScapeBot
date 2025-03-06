// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CoralIntakeSequence;
import frc.robot.commands.CoralScoreSequence;
import frc.robot.commands.DriveOpenLoop;
import frc.robot.commands.IntakeAlgaeSequence;
import frc.robot.commands.ZeroYaw;
import frc.robot.commands.MoveCoralMechanism.CoralMechanismPosition;
import frc.robot.subsystems.*;
import frc.robot.utils.GamepadFilter;

@Logged
public class RobotContainer {

  //TODO move these! (buttonmapper.java)
  public static final int DRIVER_CONTROLLER_PORT = 0;
  public static final double DRIVER_CONTROLLER_DEADBAND = 0.1;
  
  //subsystems
  private final Drivetrain drivetrain;
  private final CoralMechanism coralMechanism;
  private final AlgaeMechanism algaeMechanism;
  private final Elevator elevator;
  
  //TODO move these!
  private final CommandXboxController gamepad;
  private final GamepadFilter gamepadFilter;

  public RobotContainer() {
    drivetrain = new Drivetrain();
    coralMechanism = new CoralMechanism();
    algaeMechanism = new AlgaeMechanism();
    elevator = new Elevator();
    //these controls are temporary, todo decide omnicontrol implementation if any
    gamepad = new CommandXboxController(DRIVER_CONTROLLER_PORT);
    gamepadFilter = new GamepadFilter(gamepad, DRIVER_CONTROLLER_DEADBAND);

    //TODO modify drive command args such that final arg is a member var/can be modified elsewhere
    //TODO to meet driver preference for hold/toggle
      drivetrain.setDefaultCommand(
        new DriveOpenLoop(
          drivetrain,
          gamepadFilter::getX,
          gamepadFilter::getY,
          gamepadFilter::getTheta,
          () -> gamepad.rightBumper().getAsBoolean()));

    //puts the zero yaw command as a button on the dashboard
    // SmartDashboard.putData(new ZeroYaw(drivetrain));

    configureBindings();
  }

  private void configureBindings() {
    gamepad.leftTrigger().whileTrue(new CoralIntakeSequence(coralMechanism));
    gamepad.rightTrigger().whileTrue(new CoralScoreSequence(coralMechanism, CoralMechanismPosition.SCORE_L2));

    //gamepad.a().onTrue(new IntakeAlgaeSequence(elevator, algaeMechanism));
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
