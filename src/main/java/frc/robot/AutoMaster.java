package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

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
    static SendableChooser<PathPlannerAuto> ppAutoSelector;
    public static Command chosenAuto; //does this need to be public static?

    public AutoMaster(){
        registerNamedCommands();
        setupAutos();
    }

    /**
     * Want to use a command in PathPlanner? You're in the right place
     */
    static void registerNamedCommands(){
        NamedCommands.registerCommand("SadCoral", new SetCoralAngle(SCORE_L4, CANCEL_IMMEDIATELY));
        NamedCommands.registerCommand("AutoRaiseL1", new PrepareToScore(CORAL_L1));
        NamedCommands.registerCommand("AutoRaiseL2", new PrepareToScore(CORAL_L2));
        NamedCommands.registerCommand("AutoRaiseL3", new PrepareToScore(CORAL_L3));
        NamedCommands.registerCommand("AutoRaiseL4", new PrepareToScore(CORAL_L4));
        NamedCommands.registerCommand("AutoScore",new RunCoralMotor(OUT));
        NamedCommands.registerCommand("AutoIntake",new RunCoralMotor(IN));
    }

    static void setupAutos(){
        ppAutoSelector = new SendableChooser<PathPlannerAuto>();
        ppAutoSelector.addOption("Test", new PathPlannerAuto("L_10L"));
        ppAutoSelector.setDefaultOption("Test1", new PathPlannerAuto("SkrrA"));
        SmartDashboard.putData(ppAutoSelector);
    }

    static Command oldSchoolSequence(){
        return new SequentialCommandGroup(
            new PrintCommand("This auto routine hasn't been written yet!"),
            new WaitCommand(2),
            new PrintCommand("It really do be like that sometimes...")
        );
    }
}
