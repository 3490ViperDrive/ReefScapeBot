package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NewAlgaeManipulator;

public class RunAlgaeMotor extends Command {
    public enum Direction {
        IN, OUT
    }

    private final NewAlgaeManipulator manipulator;
    private final Direction direction;

    public RunAlgaeMotor(Direction direction) {
        this.manipulator = NewAlgaeManipulator.instance;
        this.direction = direction;
        addRequirements(manipulator);
        setName("Run Algae Motor " + direction);
    }

    @Override
    public void initialize() {
        if (direction == Direction.IN) {
            manipulator.runIntake();
        } else {
            manipulator.runOuttake();
        }
    }

    @Override
    public boolean isFinished() {
        double current = NewAlgaeManipulator.instance.getMotorCurrent();
        return current < 0; // subject to change
        // return false;
    }

    @Override
    public void end(boolean cancelled) {
        manipulator.stop();
    }

    // @Override
    // public boolean isFinished() {
    // return false;
    // }

}
