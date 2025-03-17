package frc.robot.subsystems;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
    public static Vision instance;
    PhotonCamera camera = new PhotonCamera("Jamera");
    private double yaw;
    private boolean hasTargets;
    private int tagID;
   
    //private members for
        //yaw
        //hastargets
    
    public Vision(){
        yaw = 0;
        tagID = 0;
    }

    @Override
    public void periodic(){
        instance = this; //TODO this may not actually end up being a singleton
        var result = camera.getLatestResult();
        this.hasTargets = result.hasTargets();
        List<PhotonTrackedTarget> targets = result.getTargets();
        PhotonTrackedTarget target = result.getBestTarget();

        if(hasTargets){
            this.tagID = target.getFiducialId();
            SmartDashboard.putNumber("TagID",tagID);
        } else{
            SmartDashboard.putNumber("TagID", 00);
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
}
