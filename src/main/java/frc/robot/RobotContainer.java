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
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.commands.CoralIntakeSequence;
import frc.robot.commands.CoralScoreSequence;
import frc.robot.commands.DriveOpenLoop;
import frc.robot.commands.ManualIntakeAlgae;
import frc.robot.commands.ManualOuttakeAlgae;
//import frc.robot.commands.ManualPivotDown;
//import frc.robot.commands.ManualPivot;
import frc.robot.commands.MoveCoralMechanism;
import frc.robot.commands.RunCoralIntake;
import frc.robot.commands.ZeroYaw;
import frc.robot.commands.MoveCoralMechanism.CoralMechanismPosition;
import frc.robot.commands.MoveCoralMechanism.MoveCoralCancelBehavior;
import frc.robot.commands.RunCoralIntake.CoralIntakeDirection;
import frc.robot.commands.SetElevator.SetElevatorCancelBehavior;
import frc.robot.commands.SetElevator;
import frc.robot.subsystems.*;
import frc.robot.subsystems.Elevator.ElevatorPosition;
import frc.robot.subsystems.Elevator.LogicalElevatorPosition;
import frc.robot.utils.GamepadFilter;
import frc.robot.utils.InputFilteringUtil;
import frc.robot.utils.controlProfile;

//import frc.robot.commands.ManualPivotUp;

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
  // @Logged
  // private final AlgaeMechanism algaeMechanism;
  @Logged
  private final Elevator elevator;
  @Logged
  private final Climber climber;
  
  //TODO move these!
  private final CommandXboxController driverGamepad;
  private final CommandXboxController operatorGamepad;
  private final GamepadFilter gamepadFilter;

  public RobotContainer() {
    //Subsystems
    drivetrain = new Drivetrain();
    coralMechanism = new CoralMechanism();
    //algaeMechanism = new AlgaeMechanism();
    elevator = new Elevator();
    climber = new Climber();

    //Controllers
    driverGamepad = new CommandXboxController(DRIVER_CONTROLLER_PORT);
    operatorGamepad = new CommandXboxController(OPERATOR_CONTROLLER_PORT);
    gamepadFilter = new GamepadFilter(driverGamepad, CONTROLLER_DEADBAND);

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
          () -> gamepadFilter.getX() * ((driverGamepad.leftBumper().getAsBoolean()) ? 0.4 : 1), //TODO WET!!!!
          () -> gamepadFilter.getY() * ((driverGamepad.leftBumper().getAsBoolean()) ? 0.4 : 1),
          () -> gamepadFilter.getTheta() * ((driverGamepad.leftBumper().getAsBoolean()) ? 0.4 : 1),
          () -> driverGamepad.rightBumper().getAsBoolean()));

      // algaeMechanism.setDefaultCommand(
      //   new RunCommand(() -> {
      //     //ugly!
      //     algaeMechanism.runAlgaePivot(-InputFilteringUtil.squareInput(InputFilteringUtil.applyDeadbandSpecial(operatorGamepad.getLeftY(), CONTROLLER_DEADBAND)) * 12);
      //   }, algaeMechanism) //requiring AlgaeMechanism intentionally omitted
      // );

    //Dashboard Commands
    SmartDashboard.putData(new ZeroYaw(drivetrain));
    //no more broken motor incidents!
    SmartDashboard.putData(new MoveCoralMechanism(coralMechanism, CoralMechanismPosition.SUPER_STOWED, MoveCoralCancelBehavior.CANCEL_IMMEDIATELY));
    SmartDashboard.putData(new MoveCoralMechanism(coralMechanism, CoralMechanismPosition.SCORE_L2, MoveCoralCancelBehavior.CANCEL_IMMEDIATELY));
    
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

    driverGamepad.leftTrigger().whileTrue(new CoralIntakeSequence(coralMechanism, elevator));
    driverGamepad.rightTrigger().whileTrue(new CoralScoreSequence(coralMechanism, elevator));

    driverGamepad.povUp().onTrue(new InstantCommand(() -> climber.triggerSolenoid(0)));
    driverGamepad.povDown().onTrue(new InstantCommand(() -> climber.triggerSolenoid(1)));
    // gamepad.povLeft().onTrue(new InstantCommand(() -> climber.triggerSolenoid(2))); //default

    driverGamepad.back().onTrue(sillyElevatorCmd(LogicalElevatorPosition.L4, ElevatorPosition.CORAL_L4));
    driverGamepad.start().onTrue(sillyElevatorCmd(LogicalElevatorPosition.L3, ElevatorPosition.CORAL_L3));
    driverGamepad.leftStick().onTrue(sillyElevatorCmd(LogicalElevatorPosition.L1, ElevatorPosition.CORAL_L1));
    driverGamepad.rightStick().onTrue(sillyElevatorCmd(LogicalElevatorPosition.L2, ElevatorPosition.CORAL_L2));

    operatorGamepad.a().whileTrue(new RunCoralIntake(coralMechanism, CoralIntakeDirection.IN));
    operatorGamepad.b().whileTrue(new RunCoralIntake(coralMechanism, CoralIntakeDirection.OUT));

    // gamepad.x().whileTrue(new ManualPivotDown(algaeMechanism));
    // gamepad.y().whileTrue(new ManualPivotUp(algaeMechanism));

    //operatorGamepad.leftTrigger().whileTrue(new ManualIntakeAlgae(algaeMechanism));
    //operatorGamepad.rightTrigger().whileTrue(new ManualOuttakeAlgae(algaeMechanism));

    //sidestepping potentially non-functional commands
    // operatorGamepad.leftTrigger().whileTrue(new StartEndCommand(() -> algaeMechanism.runAlgaeIntake(12), () -> algaeMechanism.stopAlgaeIntake()));
    // operatorGamepad.rightTrigger().whileTrue(new StartEndCommand(() -> algaeMechanism.runAlgaeIntake(-12), () -> algaeMechanism.stopAlgaeIntake()));
  
  }

    

    //gamepad.povLeft().whileTrue(new ManualPivot(algaeMechanism, ));
    

  private Command sillyElevatorCmd(LogicalElevatorPosition logicalPosition, ElevatorPosition realPosition) {
    return new SequentialCommandGroup(
      new ParallelCommandGroup(
        new InstantCommand(() -> elevator.setLogicalElevatorPosition(logicalPosition)),
        new SetElevator(elevator, realPosition, SetElevatorCancelBehavior.CANCEL_SETPOINT_REACHED),
        new MoveCoralMechanism(coralMechanism, () -> CoralScoreSequence.mapLogicalToCoral(logicalPosition), MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED) 
      ),
      new PrintCommand("Elevator set to " + logicalPosition)
    );
  }

  public Command getAutonomousCommand(){
    return new DriveOpenLoop(drivetrain, () -> 0.185, () -> 0, () -> 0, () -> true).withTimeout(1.15);
    //return new DriveOpenLoop(drivetrain, () -> 0.75, () -> 0, () -> 0, () -> true).withTimeout(1.5);
    //return new SequentialCommandGroup(DriveOpenLoop(drivetrain, () -> 0.225, () -> 0, () -> 0, () -> true).withTimeout(1.5),)
  }
}
