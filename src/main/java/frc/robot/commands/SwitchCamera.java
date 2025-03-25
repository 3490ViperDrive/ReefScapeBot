package frc.robot.commands;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;

public class SwitchCamera extends Command {
    UsbCamera coralCamera = CameraServer.startAutomaticCapture(0);
    UsbCamera climberCamera = CameraServer.startAutomaticCapture(1);
    NetworkTableEntry cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");
    public static boolean whichCam;

    public SwitchCamera(boolean thisCam){
        thisCam = whichCam;
    }

    @Override
    public void initialize(){
        if(whichCam){
            cameraSelection.setString(coralCamera.getName());
        } else{
            cameraSelection.setString(climberCamera.getName());
        }
    }

    public boolean getCam(){
        return this.whichCam;
    }
}
