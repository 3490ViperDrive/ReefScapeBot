package frc.robot.subsystems;

import org.photonvision.EstimatedRobotPose;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.subsystems.VisionCamera;
import frc.robot.BobotState;
import frc.robot.RobotConstants.VisionConstants;

//@Logged
public class VisionSubsystem extends SubsystemBase {
   private static String[] cameraNames = { "frontLeftCamera", "frontRightCamera"};
    public static VisionCamera[] cameras = new VisionCamera[2];

    public VisionSubsystem() {
        // Create as many camera instances as you have in the array cameraNames
        for (int i = 0; i < cameraNames.length; i++) {
            cameras[i] = new VisionCamera(cameraNames[i], VisionConstants.CAMERA_POSITIONS[i]);
            }
        }
        

    public static EstimatedRobotPose[] getVisionPoses() {
        EstimatedRobotPose[] list = new EstimatedRobotPose[3];
        for (int i = 0; i < cameraNames.length; i++) {
            try {
                    list[i] = cameras[i].getEstimatedGlobalPose(BobotState.bobotPose);
                }
                
            catch (Exception e) {
                list[i] = null;
            }
        }
        return list;
    }

    public static int getLengthOfCameraList() {
        return cameras.length;
    }

    @Override
    public void periodic() {
            if (RobotBase.isReal()) {

            }
        }
}
