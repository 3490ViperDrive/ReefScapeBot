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
import frc.robot.Enums.CoralEnums.*;
import static frc.robot.Enums.CoralEnums.CoralMechanismPosition.*;
import static frc.robot.Enums.CoralEnums.CoralIntakeDirection.*;
import static frc.robot.Enums.CoralEnums.MoveCoralCancelBehavior.*;
import frc.robot.Enums.ElevatorEnums.*;
import static frc.robot.Enums.ElevatorEnums.ElevatorPosition.*;
import static frc.robot.Enums.ElevatorEnums.TargetLevel.*;
import frc.robot.Enums.GeneralEnums.ControlProfile;
import frc.robot.utils.GamepadFilter;
import frc.robot.utils.controlProfile;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;


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
  @Logged
  private final Climber climber;

  private final Vision vision;
  

  private final CommandXboxController driverGamepad;
  private final CommandXboxController operatorGamepad;
  private final GamepadFilter gamepadFilter;

  SendableChooser<ControlProfile> controlSelector;
  SendableChooser<PathPlannerAuto> autoSelector;
  ControlProfile currentProfile;

  


  

  public RobotContainer() {
    //Subsystems
    drivetrain = new Drivetrain();
    coralMechanism = new CoralMechanism();
    elevator = new Elevator();
    climber = new Climber();
    vision = new Vision();

    //Controllers
    driverGamepad = new CommandXboxController(DRIVER_CONTROLLER_PORT);
    operatorGamepad = new CommandXboxController(OPERATOR_CONTROLLER_PORT);
    gamepadFilter = new GamepadFilter(driverGamepad, CONTROLLER_DEADBAND);


    controlSelector = new SendableChooser<ControlProfile>();
    for(ControlProfile profile : ControlProfile.values()){
      controlSelector.addOption(profile.toString(), profile);
    }

    //TODO WARNING: DO NOT MOVE THESE COMMANDS FROM THIS SPOT.
    //TODO If they do not come before the autoSelector setup, none of the PathPlanner autos will work.
    //TODO EVERY PP AUTO ROUTINE SHOULD START WITH THE SAD CORAL COMMAND
    NamedCommands.registerCommand("SadCoral", new SetCoralAngle(coralMechanism, SCORE_L4, CANCEL_IMMEDIATELY));

    NamedCommands.registerCommand("AutoRaiseL1", new PrepareToScore(elevator, coralMechanism, L1, CORAL_L1));
    NamedCommands.registerCommand("AutoRaiseL2", new PrepareToScore(elevator, coralMechanism, L2, CORAL_L2));
    NamedCommands.registerCommand("AutoRaiseL3", new PrepareToScore(elevator, coralMechanism, L3, CORAL_L3));
    NamedCommands.registerCommand("AutoRaiseL4", new PrepareToScore(elevator, coralMechanism, L4, CORAL_L4));
    NamedCommands.registerCommand("AutoScore",new RunCoralIntake(coralMechanism, CoralIntakeDirection.OUT));
    NamedCommands.registerCommand("AutoIntake",new RunCoralIntake(coralMechanism, CoralIntakeDirection.IN));

    autoSelector = new SendableChooser<PathPlannerAuto>();
    autoSelector.addOption("Test", new PathPlannerAuto("ShivaShrimple"));
    autoSelector.setDefaultOption("Test1", new PathPlannerAuto("SkrrA"));
    SmartDashboard.putData(autoSelector);

    SmartDashboard.putData("Controls", controlSelector);
    controlSelector.setDefaultOption("Default", ControlProfile.COMP);
    //currentProfile = controlSelector.getSelected();



    


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
    SmartDashboard.putData(new SetCoralAngle(coralMechanism, CoralMechanismPosition.SUPER_STOWED, MoveCoralCancelBehavior.CANCEL_IMMEDIATELY));
    SmartDashboard.putData(new SetCoralAngle(coralMechanism, CoralMechanismPosition.SCORE_L2, MoveCoralCancelBehavior.CANCEL_IMMEDIATELY));
    
    configureBindings();
  }

  private void configureBindings() {
    currentProfile = controlSelector.getSelected();
    switch (currentProfile) {
      case COMP:
      driverGamepad.leftTrigger().whileTrue(new GrabCoralSequence(coralMechanism, elevator));
      driverGamepad.rightTrigger().whileTrue(new ScoreCoralSequence(coralMechanism, elevator));
      driverGamepad.povUp().onTrue(new InstantCommand(() -> climber.triggerSolenoid(0)));
      driverGamepad.povDown().onTrue(new InstantCommand(() -> climber.triggerSolenoid(1)));
      driverGamepad.back().onTrue(new PrepareToScore(elevator, coralMechanism, TargetLevel.L4, ElevatorPosition.CORAL_L4));
      driverGamepad.start().onTrue(new PrepareToScore(elevator, coralMechanism, TargetLevel.L3, ElevatorPosition.CORAL_L3));
      driverGamepad.leftStick().onTrue(new PrepareToScore(elevator, coralMechanism, TargetLevel.L1, ElevatorPosition.CORAL_L1));
      driverGamepad.rightStick().onTrue(new PrepareToScore(elevator, coralMechanism, TargetLevel.L2, ElevatorPosition.CORAL_L2));
      operatorGamepad.a().whileTrue(new RunCoralIntake(coralMechanism, CoralIntakeDirection.IN));
      operatorGamepad.b().whileTrue(new RunCoralIntake(coralMechanism, CoralIntakeDirection.OUT));

      driverGamepad.x().whileTrue(new MoveElevatorManually(-3));
      driverGamepad.y().whileTrue(new MoveElevatorManually(3));
        break;
      case REVAMP:
      //TODO change PrepToScore command to accept a single argument (which level to prep)
      driverGamepad.x().onTrue(new PrepareToScore(elevator, coralMechanism, L1, CORAL_L1));
      driverGamepad.y().onTrue(new PrepareToScore(elevator, coralMechanism, L2, CORAL_L2));
      driverGamepad.a().onTrue(new PrepareToScore(elevator, coralMechanism, L3, CORAL_L3));
      driverGamepad.b().onTrue(new PrepareToScore(elevator, coralMechanism, L4, CORAL_L4));
      driverGamepad.povUp().whileTrue(new SnapToTarget(vision, drivetrain));
      driverGamepad.leftBumper().onTrue(new GrabCoralSequence(coralMechanism, elevator)); //TODO whileTrue()???
      driverGamepad.povDown().onTrue(new InstantCommand(()-> climber.triggerSolenoid(1))); //TODO why not use "lift"

      new Trigger(()-> driverGamepad.getRightTriggerAxis() > 0.5).onTrue(new RunCoralIntake(coralMechanism, CoralIntakeDirection.OUT));
      new Trigger(()-> driverGamepad.getLeftTriggerAxis() > 0.5).onTrue(new RunCoralIntake(coralMechanism, CoralIntakeDirection.IN));
        break;
      default:
        break;

    }

  
  }

  //TODO move this out of getAutonomousCommand() as per the docs
  public Command getAutonomousCommand(){
    //return new Drive(drivetrain, () -> 0.185, () -> 0, () -> 0, () -> true).withTimeout(1.15);
    //return new PathPlannerAuto("HopeA");
    return autoSelector.getSelected();
  }
}
