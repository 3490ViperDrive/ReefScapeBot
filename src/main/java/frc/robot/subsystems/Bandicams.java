package frc.robot.subsystems;

import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class Bandicams extends SubsystemBase{
   public static Bandicams instance;
   UsbCamera coralCam;
   UsbCamera climbCam;
   
   public Bandicams(){
      instance = this;
      coralCam = Robot.coralCamera;
      climbCam = Robot.climbCamera;
   }

   public UsbCamera getCoralCam(){
      return coralCam;
   }

   public UsbCamera getClimbCam(){
      return climbCam;
   }
}
