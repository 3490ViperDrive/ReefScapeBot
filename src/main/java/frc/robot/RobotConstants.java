package frc.robot;

import edu.wpi.first.math.geometry.Transform3d;

public class RobotConstants {
    public static final class VisionConstants {


                public static final Transform3d[] CAMERA_POSITIONS = {
                                new Transform3d(
                                                // Front Left
                                                new Translation3d(
                                                                Units.inchesToMeters(5.125), // forward+
                                                                Units.inchesToMeters(7.99), // left+
                                                                Units.inchesToMeters(0), // up+
                                                new Rotation3d(
                                                                Units.degreesToRadians(0),
                                                                Units.degreesToRadians(0), // Note, these are all
                                                                                           // counter clockwise
                                                                                           // so to
                                                                                           // face up we
                                                                                           // need
                                                                                           // - ;)
                                                                Units.degreesToRadians(0))),

                                // Front Right
                                new Transform3d(
                                                new Translation3d(
                                                                Units.inchesToMeters(5.077711), // forward+
                                                                Units.inchesToMeters(-8.006511), // left+
                                                                Units.inchesToMeters(0), // up+
                                                new Rotation3d(
                                                    Units.degreesToRadians(0),
                                                    Units.degreesToRadians(0), // Note, these are all
                                                                                               // counter clockwise
                                                                                               // so to
                                                                                               // face up we
                                                                                               // need
                                                                                               // - ;)
                                                    Units.degreesToRadians(0))) };
    }
}
