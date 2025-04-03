package frc.robot;

import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

import static frc.robot.Enums.CoralEnums.CoralIntakeDirection.*;
import static frc.robot.Enums.CoralEnums.CoralMechanismAngle.SCORE_L4;
import static frc.robot.Enums.CoralEnums.MoveCoralCancelBehavior.CANCEL_IMMEDIATELY;
import static frc.robot.Enums.CoralEnums.MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED;

import frc.robot.Enums.CoralEnums.CoralIntakeDirection;
import frc.robot.commands.AutoEatCoral;
import frc.robot.commands.GrabCoralSequence;
import frc.robot.commands.PrepareToScore;
import frc.robot.commands.RunCoralMotor;
import frc.robot.commands.SetCoralAngle;

import static frc.robot.Enums.ElevatorEnums.ElevatorPosition.*;

/**
 * Put auto stuff here 
 */
public class AutoMaster {
    public static AutoMaster instance;
    //static SendableChooser<PathPlannerAuto> autoChooser;
    public final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public AutoMaster(){
        instance = this;
        configureAutonomusChooser();
        //initialize();
    }

    public static void initialize(){
        registerNamedCommands();
    }

    public void configureAutonomusChooser() {
        autoChooser.setDefaultOption("Straight L4", new PathPlannerAuto("Straight L4"));
        autoChooser.addOption("2 Coral L4", new PathPlannerAuto("Tester"));
        //autoChooser.addOption("Just forward", new PathPlannerAuto(""));

        SmartDashboard.putData("Auto", autoChooser);
    } 


    /**
     * Want to use a command in PathPlanner? You're in the right place
     */
    static void registerNamedCommands(){
        NamedCommands.registerCommand("SadCoral", new SetCoralAngle(SCORE_L4, CANCEL_SETPOINT_REACHED));
        NamedCommands.registerCommand("AutoRaiseL1", new PrepareToScore(CORAL_L1));
        NamedCommands.registerCommand("AutoRaiseL2", new PrepareToScore(CORAL_L2));
        NamedCommands.registerCommand("AutoRaiseL3", new PrepareToScore(CORAL_L3));
        NamedCommands.registerCommand("AutoRaiseL4", new PrepareToScore(CORAL_L4));
        NamedCommands.registerCommand("AutoScore",new RunCoralMotor(OUT));
        NamedCommands.registerCommand("AutoRunIntake",new RunCoralMotor(IN));
        NamedCommands.registerCommand("AutoPrepIntake", new PrepareToScore(CORAL_INTAKE));
        NamedCommands.registerCommand("AutoEatCoral", new AutoEatCoral());
        //NamedCommands.registerCommand("AutoStopIntake", new  );
    }


    static Command oldSchoolSequence(){
        return new SequentialCommandGroup(
            new PrintCommand("This auto routine hasn't been written yet!"),
            new WaitCommand(2),
            new PrintCommand("It really do be like that sometimes...")
        );
    }

    // public static Command getChosenAuto(){
    //     return autoChooser.getSelected();
    // }
}
