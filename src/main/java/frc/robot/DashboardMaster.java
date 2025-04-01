package frc.robot;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.CoralMechanism;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Elevator;

public class DashboardMaster {
    private final Drivetrain _drive;
    private final CoralMechanism _coral;
    private final Elevator _elevator;
    private final Climber _climber;
    
    public DashboardMaster(){

        _drive = new Drivetrain();
        _coral = new CoralMechanism();
        _elevator = new Elevator();
        _climber = new Climber();

        Shuffleboard.getTab("SmartDashboard")
        .add("Elevator",1)
        .withWidget(BuiltInWidgets.kCommand)
        .getEntry();

        Shuffleboard.getTab("SmartDashboard")
        .add("CoralMechanism",1)
        .withWidget(BuiltInWidgets.kCommand)
        .getEntry();

        Shuffleboard.getTab("SmartDashboard")
        .add("Drivetrain",1)
        .withWidget(BuiltInWidgets.kCommand)
        .getEntry();

        SmartDashboard.putData("Elevator", _elevator);
        SmartDashboard.putData("Coral", _coral);
        SmartDashboard.putData("Drivetrain", _drive);
     }
}
