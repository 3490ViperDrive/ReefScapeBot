// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.epilogue.NotLogged;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CoralIntakeSequence;
import frc.robot.commands.CoralScoreSequence;
import frc.robot.commands.DriveOpenLoop;
import frc.robot.commands.IntakeAlgaeSequence;
import frc.robot.commands.ZeroYaw;
import frc.robot.commands.MoveCoralMechanism.CoralMechanismPosition;
import frc.robot.commands.SetElevator.SetElevatorCancelBehavior;
import frc.robot.commands.SetElevator;
import frc.robot.subsystems.*;
import frc.robot.subsystems.Elevator.ElevatorPosition;
import frc.robot.subsystems.Elevator.LogicalElevatorPosition;
import frc.robot.utils.GamepadFilter;
import frc.robot.utils.controlProfile;

//@Logged
public class RobotContainer {

  SendableChooser<controlProfile> profileSelection;
  public static final int DRIVER_CONTROLLER_PORT = 0;
  public static final int OPERATOR_CONTROLLER_PORT = 1;
  public static final double CONTROLLER_DEADBAND = 0.1;

  //subsystems
  @Logged
  private final Drivetrain drivetrain;
  @Logged
  private final CoralMechanism coralMechanism;
  //@Logged
  //private final AlgaeMechanism algaeMechanism;
  @Logged
  private final Elevator elevator;
  @Logged
  private final Climber climber;
  
  //TODO move these!
  private final CommandXboxController gamepad;
  private final GamepadFilter gamepadFilter;

  public RobotContainer() {
    //Subsystems
    drivetrain = new Drivetrain();
    coralMechanism = new CoralMechanism();
    //algaeMechanism = new AlgaeMechanism();
    elevator = new Elevator();
    climber = new Climber();

    //Controllers
    gamepad = new CommandXboxController(DRIVER_CONTROLLER_PORT);
    gamepadFilter = new GamepadFilter(gamepad, CONTROLLER_DEADBAND);

    //Control layouts
    /*profileSelection = new SendableChooser<controlProfile>();
    for(controlProfile profile : controlProfile.values()){
        profileSelection.addOption(profile.toString(), profile);
    }
    SmartDashboard.putData(profileSelection);*/

    //Default Commands
      drivetrain.setDefaultCommand(
        new DriveOpenLoop(
          drivetrain,
          () -> gamepadFilter.getX() * ((gamepad.leftBumper().getAsBoolean()) ? 0.4 : 1), //TODO WET!!!!
          () -> gamepadFilter.getY() * ((gamepad.leftBumper().getAsBoolean()) ? 0.4 : 1),
          () -> gamepadFilter.getTheta() * ((gamepad.leftBumper().getAsBoolean()) ? 0.4 : 1),
          () -> gamepad.rightBumper().getAsBoolean()));

    //Dashboard Commands
    SmartDashboard.putData(new ZeroYaw(drivetrain));
    
    configureBindings();
  }

  private void configureBindings() {
    /*controlProfile whomst = profileSelection.getSelected();
    switch(whomst){
        case STANDARD:
            //TODO 
            
            break;
        default:
            break;
    }*/

    gamepad.leftTrigger().whileTrue(new CoralIntakeSequence(coralMechanism, elevator));
    gamepad.rightTrigger().whileTrue(new CoralScoreSequence(coralMechanism, elevator));

    gamepad.povUp().onTrue(new InstantCommand(() -> climber.triggerSolenoid(0)));
    gamepad.povDown().onTrue(new InstantCommand(() -> climber.triggerSolenoid(1)));
    // gamepad.povLeft().onTrue(new InstantCommand(() -> climber.triggerSolenoid(2))); //default

    gamepad.back().onTrue(sillyElevatorCmd(LogicalElevatorPosition.L4, ElevatorPosition.CORAL_L4));
    gamepad.start().onTrue(sillyElevatorCmd(LogicalElevatorPosition.L3, ElevatorPosition.CORAL_L3));
    gamepad.leftStick().onTrue(sillyElevatorCmd(LogicalElevatorPosition.L1, ElevatorPosition.CORAL_L1));
    gamepad.rightStick().onTrue(sillyElevatorCmd(LogicalElevatorPosition.L2, ElevatorPosition.CORAL_L2));
  }

  private Command sillyElevatorCmd(LogicalElevatorPosition logicalPosition, ElevatorPosition realPosition) {
    return new ParallelCommandGroup(
      new InstantCommand(() -> elevator.setLogicalElevatorPosition(logicalPosition)),
      new SetElevator(elevator, realPosition, SetElevatorCancelBehavior.CANCEL_IMMEDIATELY),
      new PrintCommand("Elevator set to " + logicalPosition)
    );
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
