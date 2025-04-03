// package frc.robot.subsystems;

// import java.util.ArrayList;
// import java.util.List;
// import org.photonvision.PhotonCamera;
// import org.photonvision.targeting.PhotonTrackedTarget;

// import edu.wpi.first.wpilibj2.command.SubsystemBase;


// public class Sharingan extends SubsystemBase{

//     public static Sharingan instance;
//     PhotonCamera leftCam;
//     PhotonCamera rightCam;
//     public ArrayList<PhotonCamera> bothCams = new ArrayList<PhotonCamera>();
//     public PCamInstance[] pipelines = new PCamInstance[2];

//     private double yawTolerance = 0.5;

//     //public int sharedTarget = 0;

//     public Sharingan(){
//         //Verbose code is verbose
//         leftCam = new PhotonCamera("LeftCamera");
//         rightCam = new PhotonCamera("RightCamera");
//         bothCams.add(leftCam);
//         bothCams.add(rightCam);
//         for(int i = 0; i < pipelines.length; i++){
//             pipelines[i] = new PCamInstance();
//         }
//     }

//     @Override
//     public void periodic(){
//         //Get data from cameras if there's targets there
//         for(int i = 0; i < bothCams.size(); i++){
//             var cam = bothCams.get(i);
//             var results = cam.getAllUnreadResults();
//             if(!results.isEmpty()){
//                 var result = results.get(results.size() - 1); //certified bruh moment
//                 if(result.hasTargets()){
//                     var target = result.getBestTarget();
//                     pipelines[i].setYaw(target.getYaw());
//                     //TODO set target distance;
//                 }
//             }
//         }

//         //checkEquilibrium();

//         //check if the targets are equal

//     }

//     // public boolean checkEquilibrium(){
//     //     //determine
//     // }


// }
