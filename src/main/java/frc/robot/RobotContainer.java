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
import frc.robot.commands.*;
import frc.robot.subsystems.*;
import frc.robot.Enums.CoralEnums.*;
import frc.robot.Enums.ElevatorEnums.*;
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
  @Logged
  private final Elevator elevator;
  @Logged
  private final Climber climber;
  

  private final CommandXboxController driverGamepad;
  private final CommandXboxController operatorGamepad;
  private final GamepadFilter gamepadFilter;

  public RobotContainer() {
    //Subsystems
    drivetrain = new Drivetrain();
    coralMechanism = new CoralMechanism();
    elevator = new Elevator();
    climber = new Climber();

    //Controllers
    driverGamepad = new CommandXboxController(DRIVER_CONTROLLER_PORT);
    operatorGamepad = new CommandXboxController(OPERATOR_CONTROLLER_PORT);
    gamepadFilter = new GamepadFilter(driverGamepad, CONTROLLER_DEADBAND);

    


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
  
  }

  //TODO and then, the Lord said, "we have 10 days, it's PathPlanner time baby"
  public Command getAutonomousCommand(){
    return new Drive(drivetrain, () -> 0.185, () -> 0, () -> 0, () -> true).withTimeout(1.15);
  }
}
