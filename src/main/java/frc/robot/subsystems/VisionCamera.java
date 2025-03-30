package frc.robot.subsystems;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.CowboyUtils;

import java.util.List;
import java.util.Optional;

public class VisionCamera extends SubsystemBase {
    public PhotonCamera photonCamera;
    public PhotonPoseEstimator photonPoseEstimator;

    public VisionCamera(String cameraName, Transform3d positionTransform3d) {
        photonCamera = new PhotonCamera(cameraName);

        photonPoseEstimator = new PhotonPoseEstimator(
            CowboyUtils.aprilTagFieldLayout,
            PoseStrategy.CLOSEST_TO_LAST_POSE,
            positionTransform3d);
    }

    public void setPipeline(int index){
        photonCamera.setPipelineIndex(index);
    }

    public boolean isCameraConnected() {
        return photonCamera.isConnected();
    }

    public List<PhotonPipelineResult> getResult() {
        return photonCamera.getAllUnreadResults();
    }

    public Boolean hasResults() {
        return photonCamera.getAllUnreadResults().get(0).hasTargets();
    }

    public PhotonTrackedTarget getBestTarget() {
        return getResult().get(0).getBestTarget();
    }

    public double getTargetYaw() {
        return getBestTarget().getYaw();
    }

    public EstimatedRobotPose getEstimatedGlobalPose(Pose2d prevEstimatedRobotPose) {

        photonPoseEstimator.setLastPose(prevEstimatedRobotPose);
            try {
                List<PhotonPipelineResult> result = photonCamera.getAllUnreadResults(); // Only want to call this once
                                                                                        // per loop.

                if (result.size() > 0) {
                    Optional<EstimatedRobotPose> estimate = photonPoseEstimator
                            .update(result.get(0));

                    if (estimate.isPresent()) {
                        return estimate.get(); //skip processing for now
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }

            } catch (Exception e) {
                System.out.println(e);
                return null;
            } 
    }
}