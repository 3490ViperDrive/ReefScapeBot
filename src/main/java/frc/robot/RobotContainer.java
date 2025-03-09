// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.MoveCoralMechanism.CoralMechanismPosition;
import frc.robot.subsystems.*;
import frc.robot.Enums.*;
import frc.robot.commands.*;
import frc.robot.commands.IntakeAlgae.AlgaeIntakeDirection;
import frc.robot.utils.GamepadFilter;


@Logged
public class RobotContainer {

  //DASHBOARD VARIABLES
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

    //Dashboard 
    profileSelection.setDefaultOption("Bruh", controlProfile.HARTSVILLE);
    SmartDashboard.putData(profileSelection);
    SmartDashboard.putData(new ZeroYaw(drivetrain));
    

    //Default Commands
      drivetrain.setDefaultCommand(
        new DriveOpenLoop(
          drivetrain,
          gamepadFilter::getX,
          gamepadFilter::getY,
          gamepadFilter::getTheta,
          () -> gamepad.rightBumper().getAsBoolean()));

    configureBindings();
  }

  private void configureBindings() {
    controlProfile whomst = profileSelection.getSelected();
    switch(whomst){
        case HARTSVILLE:
            
            break;

        case FACEBUTTON_CORAL:
            new Trigger(()-> gamepad.getLeftTriggerAxis() >= 0.5).onTrue(new GrabAlgae(algaeMechanism, AlgaeLevel.LOW));
            new Trigger(()-> gamepad.getRightTriggerAxis()>= 0.5).onTrue(new GrabAlgae(algaeMechanism, AlgaeLevel.HIGH));
            gamepad.x().onTrue(new CoralScoreSequence(coralMechanism, CoralMechanismPosition.SCORE_L1));
            gamepad.y().onTrue(new CoralScoreSequence(coralMechanism, CoralMechanismPosition.SCORE_L2));
            gamepad.a().onTrue(new CoralScoreSequence(coralMechanism, CoralMechanismPosition.SCORE_L3));
            gamepad.b().onTrue(new CoralScoreSequence(coralMechanism, CoralMechanismPosition.SCORE_L4));
            gamepad.leftBumper().onTrue(new CoralIntakeSequence(coralMechanism));
            gamepad.rightBumper().onTrue(new ScoreAlgae(algaeMechanism));
            gamepad.start().onTrue(new Lift(climber, 1));
          break;
        case ACTUAL_SMARTSCORE:
          gamepad.leftBumper().onTrue(elevator.setTarget(elevator.);)

          break;
        default:
            break;
    }
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
