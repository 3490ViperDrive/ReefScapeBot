package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;

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
        algaeMotorLeft = new SparkMax(HardwareIds.Can.ALGAE_INTAKE_MOTOR_LEFT, MotorType.kBrushless);
        algaeMotorRight = new SparkMax(HardwareIds.Can.ALGAE_INTAKE_MOTOR_RIGHT, MotorType.kBrushless);

        algaePivotMotor = new SparkMax(HardwareIds.Can.ALGAE_PIVOT_MOTOR, MotorType.kBrushless);
     }

    //TODO: Write method runIntake (run the motor) and use that in commands
    //TODO: Write execute in commands to call the runIntake
    //TODO: Have magic set points for algae pivot here and call in commands
}
