//Have List of april tags to focus on for each alliance
//have them filter in vision
//only focus on them

package frc.robot.subsystems;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
    public static Vision instance;
    PhotonCamera camera = new PhotonCamera("TagCamera");
    private double yaw;
    private boolean hasTargets;
    private int tagID;

    private double visionKp;
   
    //private members for
        //yaw
        //hastargets
    
    public Vision(){
        yaw = 0;
        tagID = 0;
        visionKp = 5;
    }

    @Override
    public void periodic(){
        instance = this; //TODO this may not actually end up being a singleton
        // var result = camera.getAllUnreadResults();
        var result = camera.getLatestResult();
        this.hasTargets = result.hasTargets();
        //List<PhotonTrackedTarget> targets = result.getTargets();
        //PhotonTrackedTarget target = result.getBestTarget();

        DriverStation.getAlliance().ifPresent((alliance) -> {
            // uh something idk
        });

        if(hasTargets){
            PhotonTrackedTarget target = result.getBestTarget();
            this.tagID = target.getFiducialId();
            SmartDashboard.putNumber("TagID",tagID);
            SmartDashboard.putBoolean("Targets??", hasTargets);
            SmartDashboard.putNumber("Target Yaw", target.getYaw());
            //new
           // this.yaw = target.getYaw();
        } else{
            SmartDashboard.putNumber("TagID", 00);
            SmartDashboard.putBoolean("Targets??", hasTargets);
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
