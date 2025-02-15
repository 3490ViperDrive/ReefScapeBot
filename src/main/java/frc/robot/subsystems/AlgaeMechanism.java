package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.spark.SparkBase;

import frc.robot.HardwareIds;

public class AlgaeMechanism extends SubsystemBase {

    SparkMax algaeMotors;
    SparkMax algaePivot;
    MotorType type;

     public AlgaeMechanism() {
        algaeMotors = new SparkMax(HardwareIds.Can.ALGAE_INTAKE_MOTOR1, MotorType.kBrushless);
        algaeMotors = new SparkMax(HardwareIds.Can.ALGAE_INTAKE_MOTOR2, MotorType.kBrushless);

        algaePivot = new SparkMax(HardwareIds.Can.ALGAE_INTAKE_PIVOT, MotorType.kBrushed);
     } 
    
    @Override
    public void periodic() {};
}
