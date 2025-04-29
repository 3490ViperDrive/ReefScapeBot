package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;

public class BobotState {
    
    public static Pose2d bobotPose = new Pose2d();

    public static void updatePose(Pose2d pose) {
        bobotPose = pose;
    }
}
