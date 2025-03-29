// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import static frc.robot.Enums.CoralEnums.CoralMechanismAngle.*;
import static frc.robot.Enums.CoralEnums.CoralIntakeDirection.*;
import static frc.robot.Enums.CoralEnums.MoveCoralCancelBehavior.*;
import static frc.robot.Enums.ElevatorEnums.ElevatorPosition.*;

import com.pathplanner.lib.commands.PathPlannerAuto;

import frc.robot.Enums.GeneralEnums.ControlProfile;
import frc.robot.utils.GamepadFilter;
import frc.robot.utils.controlProfile;


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
  @Logged
  private final Elevator elevator;
  //@Logged
  private final Climber climber;

  private final Vision vision;
  private final Bandicams hypercam;
  
  private final CommandXboxController driverGamepad;
  private final CommandXboxController operatorGamepad;
  private final GamepadFilter gamepadFilter;

  SendableChooser<ControlProfile> controlSelector;
  //TODO make a dashboard initializer with Daniel's layout (+ any mods driveteam asks for)
  ControlProfile currentProfile;

  
  public RobotContainer() {
    //Subsystems
    drivetrain = new Drivetrain();
    coralMechanism = new CoralMechanism();
    elevator = new Elevator();
    climber = new Climber();
    vision = new Vision();
    hypercam = new Bandicams();

    AutoMaster.initialize(); //TODO wee bit of spaghetti here

    //Controllers
    driverGamepad = new CommandXboxController(DRIVER_CONTROLLER_PORT);
    operatorGamepad = new CommandXboxController(OPERATOR_CONTROLLER_PORT);
    gamepadFilter = new GamepadFilter(driverGamepad, CONTROLLER_DEADBAND);


    controlSelector = new SendableChooser<ControlProfile>();
    for(ControlProfile profile : ControlProfile.values()){
      controlSelector.addOption(profile.toString(), profile);
    }

    SmartDashboard.putData("Control Profile", controlSelector);
    controlSelector.setDefaultOption("Default", ControlProfile.COMP);

    //Default Commands
    drivetrain.setDefaultCommand(
        new Drive(
          drivetrain,
          () -> gamepadFilter.getX() * ((driverGamepad.leftBumper().getAsBoolean()) ? 0.4 : 1), //TODO WET!!!!
          () -> gamepadFilter.getY() * ((driverGamepad.leftBumper().getAsBoolean()) ? 0.4 : 1),
          () -> gamepadFilter.getTheta() * ((driverGamepad.leftBumper().getAsBoolean()) ? 0.4 : 1),
          () -> driverGamepad.rightBumper().getAsBoolean()));
          
    //Dashboard Commands
    SmartDashboard.putData(new ZeroYaw(drivetrain));
    SmartDashboard.putData(new SetCoralAngle(SUPER_STOWED, CANCEL_IMMEDIATELY));
    SmartDashboard.putData(new SetCoralAngle(SCORE_L2, CANCEL_IMMEDIATELY));
    
    configureBindings();
  }

  private void configureBindings() {
    currentProfile = controlSelector.getSelected();
    switch (currentProfile) {
      case COMP:
      driverGamepad.leftTrigger().whileTrue(new AutoEatCoral());
      driverGamepad.rightTrigger().whileTrue(new AutoDumpCoral());
      driverGamepad.povUp().onTrue(new InstantCommand(() -> climber.triggerSolenoid(0)));
      driverGamepad.povDown().onTrue(new InstantCommand(() -> climber.triggerSolenoid(1)));
      driverGamepad.back().onTrue(new PrepareToScore(CORAL_L4));
      driverGamepad.start().onTrue(new PrepareToScore(CORAL_L3));
      driverGamepad.leftStick().onTrue(new PrepareToScore(CORAL_L1));
      driverGamepad.rightStick().onTrue(new PrepareToScore(CORAL_L2));
      driverGamepad.x().whileTrue(new ZTarget(driverGamepad.getLeftX(), 0));
      driverGamepad.y().onTrue(new SwitchCamera());

      operatorGamepad.leftTrigger().whileTrue(new RunCoralMotor(IN));
      operatorGamepad.rightTrigger().whileTrue(new RunCoralMotor(OUT));
      operatorGamepad.povUp().whileTrue(new MoveElevatorManually(4));
      operatorGamepad.povDown().whileTrue(new MoveElevatorManually(-4));
      operatorGamepad.a().onTrue(new InstantCommand(() -> climber.triggerSolenoid(1)));
      operatorGamepad.b().onTrue(new InstantCommand(() -> climber.triggerSolenoid(0)));
      operatorGamepad.povRight().whileTrue(new MoveCoralManually(4));
      operatorGamepad.povLeft().whileTrue(new MoveCoralManually(-4));
      operatorGamepad.leftBumper().onTrue(new SetCoralAngle(SUPER_STOWED, CANCEL_IMMEDIATELY));
      operatorGamepad.x().onTrue(new PrepareToScore(CORAL_L3));
      operatorGamepad.y().onTrue(new PrepareToScore(CORAL_INTAKE));
      //operatorGamepad.leftStick().whileTrue(new GrabCoralSequence(coralMechanism, elevator));
      //operatorGamepad.rightStick().whileTrue(new ScoreCoralSequence(coralMechanism, elevator));
      operatorGamepad.rightBumper().onTrue(new ZeroYaw(drivetrain));

        break;
      case REVAMP:
      //TODO change PrepToScore command to accept a single argument (which level to prep)
      driverGamepad.x().onTrue(new PrepareToScore(CORAL_L1));
      driverGamepad.y().onTrue(new PrepareToScore(CORAL_L2));
      driverGamepad.a().onTrue(new PrepareToScore(CORAL_L3));
      driverGamepad.b().onTrue(new PrepareToScore(CORAL_L4));
      driverGamepad.povDown().onTrue(new InstantCommand(()-> climber.triggerSolenoid(1))); //TODO why not use "lift"

      new Trigger(()-> driverGamepad.getRightTriggerAxis() > 0.5).onTrue(new RunCoralMotor(OUT));
      new Trigger(()-> driverGamepad.getLeftTriggerAxis() > 0.5).onTrue(new RunCoralMotor(IN));
        break;
      default:
        break;

    }

  
  }

  public Command getAutonomousCommand(){
    //TODO move into AutoMaster(?)
    //return AutoMaster.chosenAuto;
    return new PathPlannerAuto("Tester");
  }
}
