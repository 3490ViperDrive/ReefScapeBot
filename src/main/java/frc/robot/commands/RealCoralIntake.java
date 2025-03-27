package frc.robot.commands;

import static frc.robot.Enums.ElevatorEnums.ElevatorPosition.CORAL_INTAKE;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Enums.CoralEnums.CoralIntakeDirection;

public class RealCoralIntake extends SequentialCommandGroup{
    public RealCoralIntake(){
        this.addCommands(
            new PrepareToScore(CORAL_INTAKE),
            new AutoEatCoral());
    }

}
