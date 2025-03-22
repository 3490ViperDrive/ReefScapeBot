package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import static frc.robot.Enums.CoralEnums.CoralIntakeDirection.*;
import static frc.robot.Enums.CoralEnums.CoralMechanismAngle.SCORE_L4;
import static frc.robot.Enums.CoralEnums.MoveCoralCancelBehavior.CANCEL_IMMEDIATELY;

import frc.robot.commands.PrepareToScore;
import frc.robot.commands.RunCoralMotor;
import frc.robot.commands.SetCoralAngle;

import static frc.robot.Enums.ElevatorEnums.ElevatorPosition.*;

/**
 * Put auto stuff here 
 */
public class AutoMaster {
    SendableChooser<PathPlannerAuto> ppAutoSelector;
    public AutoMaster(){
        registerNamedCommands();
    }

    static void registerNamedCommands(){
    NamedCommands.registerCommand("SadCoral", new SetCoralAngle(SCORE_L4, CANCEL_IMMEDIATELY));
    NamedCommands.registerCommand("AutoRaiseL1", new PrepareToScore(CORAL_L1));
    NamedCommands.registerCommand("AutoRaiseL2", new PrepareToScore(CORAL_L2));
    NamedCommands.registerCommand("AutoRaiseL3", new PrepareToScore(CORAL_L3));
    NamedCommands.registerCommand("AutoRaiseL4", new PrepareToScore(CORAL_L4));
    NamedCommands.registerCommand("AutoScore",new RunCoralMotor(OUT));
    NamedCommands.registerCommand("AutoIntake",new RunCoralMotor(IN));
    }
}
