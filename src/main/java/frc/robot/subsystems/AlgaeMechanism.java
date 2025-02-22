package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//import com.revrobotics.spark.SparkLowLevel;
import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.spark.SparkBase;

import frc.robot.HardwareIds;

//import frc.robot.commands.*;

public class AlgaeMechanism extends SubsystemBase {

    SparkMax algaeMotorLeft;
    SparkMax algaeMotorRight;
    SparkMax algaePivotMotor;
    MotorType type;

     public AlgaeMechanism() {
        algaeMotorLeft = new SparkMax(HardwareIds.Can.ALGAE_INTAKE_MOTOR1, MotorType.kBrushless);
        algaeMotorRight = new SparkMax(HardwareIds.Can.ALGAE_INTAKE_MOTOR2, MotorType.kBrushless);

        algaePivotMotor = new SparkMax(HardwareIds.Can.ALGAE_PIVOT_MOTOR, MotorType.kBrushless);
     }

    
    @Override
    public void periodic() {};
}
