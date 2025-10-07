// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import static frc.robot.Enums.CoralEnums.CoralMechanismAngle.*;
import static frc.robot.Enums.CoralEnums.CoralIntakeDirection.*;
import static frc.robot.Enums.CoralEnums.MoveCoralCancelBehavior.*;
import static frc.robot.Enums.ElevatorEnums.ElevatorPosition.*;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import frc.robot.Enums.CoralEnums.CoralMechanismAngle;
import frc.robot.Enums.CoralEnums.MoveCoralCancelBehavior;
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

  @Logged
  private final NewAlgaeManipulator algaeManipulator;

  private final Vision vision;
  private final Bandicams hypercam;
  
  private final CommandXboxController driverGamepad;
  private final CommandXboxController operatorGamepad;
  private final GamepadFilter gamepadFilter;

  SendableChooser<ControlProfile> controlSelector;
  //SendableChooser<PathPlannerAuto> autoChooser;
  //TODO make a dashboard initializer with Daniel's layout (+ any mods driveteam asks for)
  ControlProfile currentProfile;
  //AutoMaster autoMaster; 

  SendableChooser<PathPlannerAuto> autoChooser;

  //private final AutoMaster autoMaster;
  
  public RobotContainer() {
    //Subsystems
    drivetrain = new Drivetrain();
    coralMechanism = new CoralMechanism();
    elevator = new Elevator();
    climber = new Climber();
    vision = new Vision();
    hypercam = new Bandicams();
    algaeManipulator = new NewAlgaeManipulator();


    //autoMaster = new AutoMaster();
    autoChooser = new SendableChooser<PathPlannerAuto>();

    //Controllers
    driverGamepad = new CommandXboxController(DRIVER_CONTROLLER_PORT);
    operatorGamepad = new CommandXboxController(OPERATOR_CONTROLLER_PORT);
    gamepadFilter = new GamepadFilter(driverGamepad, CONTROLLER_DEADBAND);


    controlSelector = new SendableChooser<ControlProfile>();
    for(ControlProfile profile : ControlProfile.values()){
      controlSelector.addOption(profile.toString(), profile);
    }

    //SmartDashboard.putData("Control Profile", controlSelector);
    controlSelector.setDefaultOption("Default", ControlProfile.COMP);
    autoChooser = new SendableChooser<PathPlannerAuto>();

    //Default Commands
    drivetrain.setDefaultCommand(
        new Drive(
          drivetrain,
          () -> gamepadFilter.getX() * ((driverGamepad.leftBumper().getAsBoolean()) ? 0.4 : 1), //TODO WET!!!!
          () -> gamepadFilter.getY() * ((driverGamepad.leftBumper().getAsBoolean()) ? 0.4 : 1),
          () -> gamepadFilter.getTheta() * ((driverGamepad.leftBumper().getAsBoolean()) ? 0.4 : 1),
          () -> driverGamepad.rightBumper().getAsBoolean()));
          
    //Dashboard Commands
    // SmartDashboard.putData(new ZeroYaw(drivetrain));
    SmartDashboard.putData(new SetCoralAngle(SUPER_STOWED, CANCEL_IMMEDIATELY));
    // SmartDashboard.putData(new SetCoralAngle(SCORE_L2, CANCEL_IMMEDIATELY));

        NamedCommands.registerCommand("SadCoral", new SetCoralAngle(SCORE_L4, CANCEL_SETPOINT_REACHED));
        NamedCommands.registerCommand("AutoRaiseL1", new PrepareToScore(CORAL_L1));
        NamedCommands.registerCommand("AutoRaiseL2", new PrepareToScore(CORAL_L2));
        NamedCommands.registerCommand("AutoRaiseL3", new PrepareToScore(CORAL_L3));
        NamedCommands.registerCommand("AutoRaiseL4", new PrepareToScore(CORAL_L4));
        NamedCommands.registerCommand("AutoScore",new RunCoralMotor(OUT));
        NamedCommands.registerCommand("AutoRunIntake",new RunCoralMotor(IN));
        NamedCommands.registerCommand("AutoPrepIntake", new PrepareToScore(CORAL_INTAKE));
        NamedCommands.registerCommand("AutoEatCoral", new AutoEatCoral());

        autoChooser = new SendableChooser<PathPlannerAuto>();

        autoChooser.setDefaultOption("Move Forward", new PathPlannerAuto ("New New Auto"));
        autoChooser.addOption("Brad Goofin", new PathPlannerAuto ("brad goofin"));
        autoChooser.addOption("Side 2 Coral", new PathPlannerAuto(""));
        autoChooser.addOption("Straight L4", new PathPlannerAuto("Straight L4"));
        autoChooser.addOption("Side L4", new PathPlannerAuto("Tester"));
        autoChooser.addOption("Do none", null);
        //autoChooser.addOption("Just forward", new PathPlannerAuto(""));

        SmartDashboard.putData("Auto", autoChooser);
    
    configureBindings();
  }

  private void configureBindings() {
    currentProfile = controlSelector.getSelected();
    switch (currentProfile) {
      case COMP:
      driverGamepad.leftTrigger().whileTrue(new GrabCoralSequence(CoralMechanism.instance, Elevator.instance));
      driverGamepad.rightTrigger().whileTrue(new RunCoralMotor(OUT));
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
      operatorGamepad.povUp().whileTrue(new MoveElevatorManually(3));
      operatorGamepad.povDown().whileTrue(new MoveElevatorManually(-3));
      //operatorGamepad.a().onTrue(new InstantCommand(() -> climber.triggerSolenoid(1)));
      //operatorGamepad.b().onTrue(new InstantCommand(() -> climber.triggerSolenoid(0)));
      operatorGamepad.povRight().whileTrue(new MoveCoralManually(3));
      operatorGamepad.povLeft().whileTrue(new MoveCoralManually(-3));
      operatorGamepad.leftBumper().onTrue(new SetCoralAngle(SUPER_STOWED, CANCEL_IMMEDIATELY));
      operatorGamepad.x().onTrue(new PrepareToScore(CORAL_L3));
      operatorGamepad.y().onTrue(new PrepareToScore(CORAL_INTAKE));
      operatorGamepad.leftStick().whileTrue(new GrabCoralSequence(CoralMechanism.instance, Elevator.instance));
      //operatorGamepad.rightStick();
      operatorGamepad.rightBumper().onTrue(new ZeroYaw(drivetrain));

      operatorGamepad.a().whileTrue(new RunAlgaeMotor(RunAlgaeMotor.Direction.OUT));
      operatorGamepad.b().whileTrue(new RunAlgaeMotor(RunAlgaeMotor.Direction.IN));


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
    // return new
    // SequentialCommandGroup(new SetCoralAngle(CoralMechanismAngle.SCORE_L3, MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED), 
    // new Drive(drivetrain, () -> 0.45, () -> 0, () -> 0, () -> true).withTimeout(6),
    // new Drive(drivetrain, ()->0, ()->0, ()->0, ()->true).withTimeout(0.5));

    return autoChooser.getSelected();


    // return AutoMaster.getChosenAuto();
    // if (AutoMaster.instance != null) {
    //   return AutoMaster.instance.autoChooser.getSelected();
    // } else {
    //   return null;
    }
  }


