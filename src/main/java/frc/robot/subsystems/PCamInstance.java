package frc.robot.subsystems;

/**
 * Container class for all relevant apriltag data returned by each camera instance. 
 * There's probably an existing solution but there's no time to look for it.
 */
public class PCamInstance {
    private double yaw;
    private double distance;

    public void setYaw(double aYaw){
        yaw = aYaw;
    }

    public double getYaw(){
        return yaw;
    }

    public void setDistance(double aDistance){
        distance = aDistance;
    }

    public double getDistance(){
        return distance;
    }
}
