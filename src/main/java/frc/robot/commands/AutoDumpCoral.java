package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.CoralMechanism;
import frc.robot.subsystems.Elevator;


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
            new SetCoralAngle(CoralMechanism.getAngleFromElevatorSetpoint(_elevator.getCurrentTarget()), CANCEL_SETPOINT_REACHED),
            new RunCoralMotor(OUT).withTimeout(0.5));
    }
}
