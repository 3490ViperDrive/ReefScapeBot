package frc.robot;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public class RobotConstants {
    
    public static final class VisionConstants {

        public static final Transform3d[] CAMERA_POSITIONS = {
            new Transform3d(
                new Translation3d(
                    Units.inchesToMeters(5.125), //forward
                    Units.inchesToMeters(7.99), //left
                    Units.inchesToMeters(0)),

                new Rotation3d(
                    Units.degreesToRadians(0),
                    Units.degreesToRadians(0),
                    Units.degreesToRadians(0))),

           new Transform3d(
                new Translation3d(
                    Units.inchesToMeters(-4.362060),
                    Units.inchesToMeters(9.3),
                    Units.inchesToMeters(0)),
                new Rotation3d(
                    Units.degreesToRadians(0),
                    Units.degreesToRadians(0),
                    Units.degreesToRadians(0))) 
            
        };
    }
}
