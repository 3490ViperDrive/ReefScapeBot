// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.DriveOpenLoop;
import frc.robot.commands.ZeroYaw;
import frc.robot.subsystems.*;
import frc.robot.utils.GamepadFilter;
import frc.robot.utils.controlProfile;

@Logged
public class RobotContainer {

  SendableChooser<controlProfile> profileSelection;
  public static final int DRIVER_CONTROLLER_PORT = 0;
  public static final double DRIVER_CONTROLLER_DEADBAND = 0.1;
  
  //subsystems
  private final Drivetrain drivetrain;
  private final CoralMechanism coralMechanism;
  private final AlgaeMechanism algaeMechanism;
  private final Elevator elevator;
  private final Climber climber;

  
  //TODO move these!
  private final CommandXboxController gamepad;
  private final GamepadFilter gamepadFilter;

  public RobotContainer() {
    //Subsystems
    drivetrain = new Drivetrain();
    coralMechanism = new CoralMechanism();
    algaeMechanism = new AlgaeMechanism();
    elevator = new Elevator();
    climber = new Climber();

    //Controllers
    gamepad = new CommandXboxController(DRIVER_CONTROLLER_PORT);
    gamepadFilter = new GamepadFilter(gamepad, DRIVER_CONTROLLER_DEADBAND);

    //Control layouts
    profileSelection = new SendableChooser<controlProfile>();
    for(controlProfile profile : controlProfile.values()){
        profileSelection.addOption(profile.toString(), profile);
    }
    SmartDashboard.putData(profileSelection);

    //Default Commands
      drivetrain.setDefaultCommand(
        new DriveOpenLoop(
          drivetrain,
          gamepadFilter::getX,
          gamepadFilter::getY,
          gamepadFilter::getTheta,
          () -> gamepad.rightBumper().getAsBoolean()));

    //Dashboard Commands
    SmartDashboard.putData(new ZeroYaw(drivetrain));
    
    configureBindings();
  }

  private void configureBindings() {
    controlProfile whomst = profileSelection.getSelected();
    switch(whomst){
        case STANDARD:
            //TODO 
            break;
        default:
            break;
    }
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
