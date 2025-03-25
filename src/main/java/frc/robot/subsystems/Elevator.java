package frc.robot.subsystems;

import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.HardwareIds;
import frc.robot.configs.ElevatorConfigs;
import static frc.robot.configs.ElevatorConfigs.*;
import static frc.robot.HardwareIds.Can.*;
import static frc.robot.Enums.ElevatorEnums.*;
import static frc.robot.Enums.ElevatorEnums.ElevatorPosition.*;

public class Elevator extends SubsystemBase {

    public static Elevator instance;
    public static ElevatorConfigs configs;
    private final TalonFX motorController;

    //TODO ironic, this 
    private ElevatorPosition currentTarget = DEFAULT;

    public static class ClosedLoopGains {
        //feedback
        public static final double P = 6;
        //gravity feedforward (static)
        public static final double G = 0.16;
    }





    public Elevator(){
        instance = this;
        configs = new ElevatorConfigs();
        motorController = new TalonFX(ELEVATOR_MOTOR);
        configs.configureMotor(motorController);
        

        //this assumes that the elevator is at the lower hardstop and the rope is taut
        //this is bad!
        motorController.setPosition(0);
        //TODO FIX THIS
    }

    public void setPosition(double rawPosition) {
        motorController.setControl(new PositionVoltage(rawPosition));
    }

    public void runOpenLoop(double volts) {
        motorController.setControl(new VoltageOut(volts));
    }

    public boolean isAtSetpoint() {
        return Math.abs(motorController.getClosedLoopError().getValueAsDouble()) < ElevatorConfigs.AT_SETPOINT_TOLERANCE;
    }

    /** 
     *  Updates the current "Target Level" of the Elevator. For use in 'automated' elevator commands.
     * @param level
     */
    // public void setTargetLevel(TargetLevel level) {
    //     this.currentTarget = level;
    // }
    //TODO just pass in the position itself and store this in a field. 

    @Logged
    public ElevatorPosition getCurrentTarget() {
        return currentTarget;
    }

    public void updateTarget(ElevatorPosition newTargetLevel){
        currentTarget = newTargetLevel;
    }
}
