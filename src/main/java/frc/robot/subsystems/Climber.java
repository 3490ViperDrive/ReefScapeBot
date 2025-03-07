package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.HardwareIds;

public class Climber extends SubsystemBase {

    public static final int FORWARD_CHANNEL = 14;
    public static final int REVERSE_CHANNEL = 15;

    DoubleSolenoid theSolenoid = new DoubleSolenoid(HardwareIds.Can.PNEUMATIC_HUB, PneumaticsModuleType.REVPH, FORWARD_CHANNEL, REVERSE_CHANNEL);

    public void triggerSolenoid(int whichDirection){
        switch (whichDirection) {
            case 0:
                theSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case 1:
                theSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
            default:  
                theSolenoid.set(DoubleSolenoid.Value.kOff);
                break;
        }

    }
}
