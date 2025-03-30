package frc.robot.subsystems;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RightCamera extends SubsystemBase {
    public static RightCamera instance;
    PhotonCamera camera = new PhotonCamera("RightCamera");
    private double yaw;
    private boolean hasTargets;
    private int tagID;

    private double visionKp;
   
    //private members for
        //yaw
        //hastargets
    
    public RightCamera(){
        instance = this; 
        yaw = 0;
        tagID = 0;
        visionKp = 5;
    }

    @Override
    public void periodic(){
        var result = camera.getLatestResult();
        this.hasTargets = result.hasTargets();
        //List<PhotonTrackedTarget> targets = result.getTargets();
        //PhotonTrackedTarget target = result.getBestTarget();

        if(hasTargets){
            PhotonTrackedTarget target = result.getBestTarget();
            this.tagID = target.getFiducialId();
            // SmartDashboard.putNumber("TagID",tagID);
            // SmartDashboard.putBoolean("Targets??", hasTargets);
            // SmartDashboard.putNumber("Target Yaw", target.getYaw());
            this.yaw = target.getYaw();
        } else{
            // SmartDashboard.putNumber("TagID", 00);
            // SmartDashboard.putBoolean("Targets??", hasTargets);
            //TODO update display on dashboard
            this.yaw = 0;
        }

    }

    public double getYaw(){
        return this.yaw;
    }

    public boolean getTargetStatus(){
        return this.hasTargets;
    }
    
    public int getTagID(){
        return this.tagID;
    }

    public double getVisionKp(){
        return visionKp;
    }
}
