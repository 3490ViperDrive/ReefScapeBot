package frc.robot;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class RobotConstants {
        public static final class VisionConstants {

                // public static final double SINGLE_TAG_CUTOFF_METERS = 4.0;

                // public static final double AMBIGUITY_CUTOFF = 0.05;

                public static final Transform3d[] CAMERA_POSITIONS = {
                                new Transform3d(
                                                // Front Left
                                                new Translation3d(
                                                                Units.inchesToMeters(5.125), // forward+
                                                                Units.inchesToMeters(7.99), // left+
                                                                Units.inchesToMeters(0)), // up+
                                                new Rotation3d(
                                                                Units.degreesToRadians(0),
                                                                Units.degreesToRadians(0), 
                                                                Units.degreesToRadians(0))),
 
                                // Front Right
                                new Transform3d(
                                                new Translation3d(
                                                                Units.inchesToMeters(-4.361060), // forward+
                                                                Units.inchesToMeters(9.375080), // left+
                                                                Units.inchesToMeters(0)), // up+
                                                new Rotation3d(
                                                                Units.degreesToRadians(0),
                                                                Units.degreesToRadians(0), 
                                                                Units.degreesToRadians(0))) };
        }
}
