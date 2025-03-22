package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Enums.CoralEnums.CoralMechanismAngle;
import frc.robot.Enums.ElevatorEnums.ElevatorPosition;
import frc.robot.subsystems.CoralMechanism;
import frc.robot.subsystems.Elevator;

import static frc.robot.Enums.CoralEnums.CoralMechanismAngle.*;
import static frc.robot.Enums.CoralEnums.MoveCoralCancelBehavior.*;
import static frc.robot.Enums.CoralEnums.CoralIntakeDirection.*;

/**
 * Changes to the elevator subsystem's implementation broke "ScoreCoralSequence"; this should provide a clean, quick fix
 */
public class AutoDumpCoral extends SequentialCommandGroup{
    Elevator _elevator;
    CoralMechanism _coralMechanism;

    public AutoDumpCoral(){
        _coralMechanism = CoralMechanism.instance;
        _elevator = Elevator.instance;
        this.addCommands(
            new SetCoralAngle(_coralMechanism, 
                getAngleFromElevatorSetpoint(_elevator.getCurrentTarget()), CANCEL_SETPOINT_REACHED),
            new RunCoralIntake(_coralMechanism, OUT));
    }

    //TODO add a default coralMechAngle
    private static CoralMechanismAngle getAngleFromElevatorSetpoint(ElevatorPosition elevatorPos){
        CoralMechanismAngle retVal = SCORE_L1;
        switch(elevatorPos){
            case ALGAE_L2:
                break;
            case ALGAE_L3:
                break;
            case CORAL_INTAKE:
                break;
            case CORAL_L1:
                retVal = SCORE_L1;
                break;
            case CORAL_L2:
                retVal = SCORE_L2;
                break;
            case CORAL_L3:
                retVal = SCORE_L3;
                break;
            case CORAL_L4:
                retVal = SCORE_L4;
                break;
            case DEFAULT:
                break;
            case PROCESSOR:
                break;
            default:
                break;
        }
        return retVal;
    }
}
