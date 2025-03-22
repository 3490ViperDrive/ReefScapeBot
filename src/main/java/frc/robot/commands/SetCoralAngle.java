package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralMechanism;
import static frc.robot.Enums.CoralEnums.*;
/**
 * Moves the coral mechanism to an angle using closed-loop controls.
 */
public class SetCoralAngle extends Command {
    private final CoralMechanism coralMechanism;
    private final Supplier<CoralMechanismAngle> setpointSup;
    private final MoveCoralCancelBehavior cancelBehavior;

    

    public SetCoralAngle(Supplier<CoralMechanismAngle> setpointSup,
                              MoveCoralCancelBehavior cancelBehavior) {
        this.coralMechanism = CoralMechanism.instance;
        this.setpointSup = setpointSup;
        this.cancelBehavior = cancelBehavior;
        super.addRequirements(coralMechanism);
        super.setName(String.format("Move Coral Mech to somewhere idk"));
    }

    public SetCoralAngle(CoralMechanismAngle setpoint,
                         MoveCoralCancelBehavior cancelBehavior) {

        this(() -> setpoint, cancelBehavior);
        super.setName("Move Coral Mech to " + setpoint);
    }

    @Override
    public void initialize() {
        coralMechanism.setPivotSetpoint(setpointSup.get().getAngle());
    }

    @Override
    public boolean isFinished() {
        if (cancelBehavior == MoveCoralCancelBehavior.CANCEL_SETPOINT_REACHED) {
            return coralMechanism.getAtSetpoint();
        } else {
            return true;
        }
    }
}
