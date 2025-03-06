package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climba extends SubsystemBase {

    DoubleSolenoid theSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH, 0, 0);

    public void triggerSolenoid(int whichDirection){
        switch (whichDirection) {
            case 0:
                theSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
            case 1:
                theSolenoid.set(DoubleSolenoid.Value.kOff);
                break;
            default:  
                theSolenoid.set(DoubleSolenoid.Value.kReverse);
                break;
        }

    }
}
