package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.HardwareIds;

public class NewAlgaeManipulator extends SubsystemBase {
    public static NewAlgaeManipulator instance;

    //private final SparkMax motor;
    private final SparkMax motor;

    public NewAlgaeManipulator() {
        instance = this;
        motor = new SparkMax(HardwareIds.Can.ALGAE_MOTOR, MotorType.kBrushless);
        //motor = new TalonFX(HardwareIds.Can.ALGAE_MOTOR);
        //motor.setIdleMode(SparkMax.IdleMode.kBrake);
    }

    public void runIntake() {
        motor.set(0.9); 
    }

    public void runOuttake() {
        motor.set(-0.9); 
    }

    public void stop() {
        motor.stopMotor();
    }

    public double getMotorCurrent() {
        //return motor.getOutputCurrent();
        return 0;
    }    

    @Override
    public void periodic() {
    SmartDashboard.putNumber("Algae Motor Current", getMotorCurrent());
}

}
