package frc.robot;

public class Enums {

    public static final class ElevatorEnums{
        public static enum ElevatorPosition {
            DEFAULT(0.0),
            CORAL_INTAKE(0.5),
            PROCESSOR(0),
            ALGAE_L2(6),
            ALGAE_L3(18),
            CORAL_L1(0.0),
            CORAL_L2(2),
            CORAL_L3(17.5),
            CORAL_L4(47.12);
    
            double position;
    
            ElevatorPosition(double position) {
                this.position = position;
            }
    
            public double getPosition() {
                return this.position;
            }
        }

        public enum SetElevatorCancelBehavior {
            CANCEL_IMMEDIATELY,
            CANCEL_SETPOINT_REACHED
        }

        public static enum TargetLevel {
            L1,
            L2,
            L3,
            L4
          }
    }

    public static class AlgaeEnums{

    }

    public static class CoralEnums{
        public static enum CoralMechanismAngle {
            SUPER_STOWED(0.26),
            STOWED(0.05),
            INTAKE(0.12),
            SCORE_L1(-0.05),
            SCORE_L2(-0.05),
            SCORE_L3(-0.05),
            SCORE_L4(-0.165);
    
            private final double angle;
    
            CoralMechanismAngle(double angle) {
                this.angle = angle;
            }
    
            public double getAngle() {
                return angle;
            }
        }

        public static enum MoveCoralCancelBehavior {
            CANCEL_IMMEDIATELY,
            CANCEL_SETPOINT_REACHED
        }

        public static enum CoralIntakeDirection {
            IN,
            OUT
        }
    }

    public static class ClimberEnums{
        
    }

    public static class GeneralEnums{
        public static enum ControlProfile{
            COMP, REVAMP
        }
    }
 
    
}
