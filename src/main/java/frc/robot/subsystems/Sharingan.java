package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class Sharingan extends SubsystemBase{

    public static Sharingan instance;
    PhotonCamera leftCam;
    PhotonCamera rightCam;
    public ArrayList<PhotonCamera> bothCams = new ArrayList<PhotonCamera>();

    public Sharingan(){
        leftCam = new PhotonCamera("LeftCamera");
        rightCam = new PhotonCamera("RightCamera");
        bothCams.add(leftCam);
        bothCams.add(rightCam);
    }

    @Override
    public void periodic(){
        //checkEquilibrium();
        for(int i = 0; i < bothCams.size(); i++){
            
        }
    }

    public boolean checkEquilibrium(){
        if
    }


}
