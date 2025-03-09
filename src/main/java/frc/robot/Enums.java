package frc.robot;
/**
 * The hardest choices require the strongest wills...
 */
public class Enums {
    
    //ELEVATOR POSITIONS
    public enum Positions{
        // TODO: set values & RENAME THIS ENUM
        DEFAULT(0.0),
        ALGAE_L2(0.0),
        ALGAE_L3(0.0),
        CORAL_L1(0.0),
        CORAL_L2(0.0),
        CORAL_L3(0.0),
        CORAL_L4(0.0);

        double position;
        Positions(double position){
            this.position = position;
        }
        public double get(){
            return this.position;
        }
    } //ELEVATOR POSITIONS

    //CONTROL PROFILES
    public enum controlProfile{
        HARTSVILLE, FACEBUTTON_CORAL, ACTUAL_SMARTSCORE
    }

    //ALGAE CHOICES
    public enum AlgaeLevel{
        LOW, HIGH
    }

    public enum AlgaeMechanismPosition {
        INITIAL(0),
        GROUND(0),
        HOLD_SCORE(0);

        double algaeAngle;

        AlgaeMechanismPosition(double algaeAngle) {
            this.algaeAngle = algaeAngle;
        }

        public double getAlgaeAngle() {
            return algaeAngle;
        }
    }

    //CORAL
    public enum CoralIntakeDirection {
        IN,
        OUT
    }

    
}
