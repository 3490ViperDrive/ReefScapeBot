// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.epilogue.Epilogue;
import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.epilogue.logging.FileBackend;
import edu.wpi.first.epilogue.logging.NTEpilogueBackend;
import edu.wpi.first.epilogue.logging.errors.ErrorHandler;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

@Logged
public class Robot extends TimedRobot {
  private Command autonomousCommand;

  private final RobotContainer robotContainer;

  public enum LoggingStrategy {
    NT_ALL,
    NT_CRITICAL_ONLY,
    FILE_ONLY
  }
  public final LoggingStrategy logStrategy = LoggingStrategy.NT_ALL;

  public Robot() {
    robotContainer = new RobotContainer();

    DriverStation.silenceJoystickConnectionWarning(true);

    //configure logging
    Epilogue.configure(config -> {
      if (isSimulation()) {
        config.errorHandler = ErrorHandler.crashOnError();
      } else {
        config.errorHandler = ErrorHandler.printErrorMessages();
      }
      switch (logStrategy) {
        case NT_ALL:
        case NT_CRITICAL_ONLY:
          config.backend = new NTEpilogueBackend(NetworkTableInstance.getDefault());
        break;
        case FILE_ONLY:
        default:
          config.backend = new FileBackend(DataLogManager.getLog());
        break;
      }
      if (logStrategy == LoggingStrategy.NT_CRITICAL_ONLY) {
        config.minimumImportance = Logged.Importance.CRITICAL;
      }
    });
    Epilogue.bind(this);
    DataLogManager.start();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    autonomousCommand = robotContainer.getAutonomousCommand();

    if (autonomousCommand != null) {
      autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
