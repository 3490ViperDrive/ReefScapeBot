// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.*;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

//import commands.algaeintake
//import commands.algaepivot
//import commands.elevatorcommand
//import commands.drivecommand
//import commands.coralintake
//import commands.coralpivot
//import commands.cagecommand
//import commands.crawlmode
//import commands.robotorientation

@Logged
public class RobotContainer {

  XboxController controller = new XboxController(0);

  private final Drivetrain drivetrain;

  public RobotContainer() {
    drivetrain = new Drivetrain();
    configureBindings();

    //algae mechanism = new algae mechanism
    //coral mechanism = new coral mechanism
    //elevator mechanism = new elevator mechanism 
  }

  private void configureBindings() {
    //drive = left thumb stick
    //turning = right thumb stick
    //elevator = 4 back paddles
    //coral intake = left trigger 
    //coral outake = right trigger
    //algae intake = b
    //algae outake = a
    //algae manual pivot = x 
    //manual elevator = d pad (to bring it up and down)
    //cage = y
    //crawl mode = left bumper
    //orientation = right bumper
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}
