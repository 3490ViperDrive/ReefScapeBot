// All credits go to 8575, who this code comes from. 
// https://github.com/DueWesternersProgramming/FRCCode-2025/blob/Individual-Poses-Automated-Score/src/main/java/frc/robot/utils/CowboyUtils.java

package frc.robot.util;

import java.io.IOException;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class CowboyUtils {

    public static final AprilTagFieldLayout aprilTagFieldLayout = AprilTagFieldLayout
    .loadField(AprilTagFields.kDefaultField);

    public static Pose2d testPose = new Pose2d(1.4, 5.55, new Rotation2d(Math.toRadians(0)));

    public static boolean isRedAlliance() {
        return DriverStation.getAlliance().isPresent() ? (DriverStation.getAlliance.get() == Alliance.Red) : (false);
    }

        public static boolean isBlueAlliance() {
        return DriverStation.getAlliance().isPresent() ? (DriverStation.getAlliance.get() == Alliance.Blue) : (false);
    }

    public static double getParallelError(Pose2d origin, Pose2d target) {
        Translation2d originToTarget = origin.minus(target).getTranslation();
        Rotation2d angleBetween = originToTarget.getAngle();
        double parallelError = originToTarget.getNorm() * angleBetween.getSin();

        return parallelError
    }

    public static double getPerpendicularError(Pose2d origin, Pose2d target) {
        Translation2d originToTarget = origin.minus(target).getTranslation();
        Rotation2d angleBetween = originToTarget.getAngle();
        double perpendicularError = originToTarget.getNorm() * angleBetween.getCos();

        return perpendicularError;
    }
}
