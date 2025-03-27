package frc.robot.utils;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;

public class AprilTagUtils {

    public static Pose2d getBranchFromTag(Pose2d tag, BranchSide side) {
        var translation = tag.getTranslation().plus(
            new Translation2d(
                side.tagOffset.getY(),
                side.tagOffset.getX()
            ).rotateBy(tag.getRotation())
        );

        return new Pose2d(
            translation.getX(),
            translation.getY(),
            tag.getRotation()
        );
    }

    public static Pose2d getClosestReefAprilTag(Pose2d pose) {
        var alliance = DriverStation.getAlliance();
        
        ArrayList<Pose2d> reefPoseList;
        if (alliance.isEmpty()) {
            reefPoseList = allReefTagPoses;
        } else{
            reefPoseList = alliance.get() == Alliance.Blue ? 
                blueReefTagPoses :
                redReefTagPoses;
        }


        return pose.nearest(reefPoseList);

    }

    public static Pose2d getClosestBranch(FieldBranchSide fieldSide, SwerveSubsystem swerve){
        Pose2d swervePose = swerve.predict(kAutoAlignPredict);
        
        Pose2d tag = getClosestReefAprilTag(swervePose);
        
        BranchSide tagSide = fieldSide.branchSide;

        if (
            swervePose.getX() > 4.500
            &&
            swervePose.getX() < 13
        ) {
            tagSide = fieldSide.getOpposite().branchSide;
        }

        return getBranchFromTag(tag, tagSide);
    }

    /**
     * 
     * @return Pathplanner waypoint with direction of travel away from the associated reef side
     */
    private Pose2d getWaypointFromBranch(Pose2d branch){
        return new Pose2d(
            branch.getTranslation(),
            branch.getRotation().rotateBy(Rotation2d.k180deg)
        );
    }

    /**
     * 
     * @return target rotation for the robot when it reaches the final waypoint
     */
    private Rotation2d getBranchRotation(SwerveSubsystem swerve){
        return getClosestReefAprilTag(swerve.getPose()).getRotation().rotateBy(Rotation2d.k180deg);
    }



}