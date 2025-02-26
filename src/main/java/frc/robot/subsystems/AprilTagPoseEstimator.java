// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class AprilTagPoseEstimator extends SubsystemBase {
  private EstimatedRobotPose prevEstimatedRobotPose = new EstimatedRobotPose(new Pose3d(new Translation3d(0, 0, 0), new Rotation3d(0, 0, 0)), 0, new ArrayList<PhotonTrackedTarget>(), PhotonPoseEstimator.PoseStrategy.LOWEST_AMBIGUITY);
  private final AprilTagFieldLayout aprilTagFieldLayout;
  private final PhotonCamera cam;
  private final PhotonPoseEstimator photonPoseEstimator;

  /** Creates a new AprilTagPoseEstimator. */
  public AprilTagPoseEstimator() {
    // The field from AprilTagFields will be different depending on the game.
    this.aprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

    //Forward Camera
    this.cam = new PhotonCamera(Constants.CVConstants.kCameraName);

    // Construct PhotonPoseEstimator
    this.photonPoseEstimator = new PhotonPoseEstimator(this.aprilTagFieldLayout, PoseStrategy.CLOSEST_TO_REFERENCE_POSE, Constants.CVConstants.kRobotToCamera);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public List<PhotonTrackedTarget> getVisibleTags() {
    this.photonPoseEstimator.setReferencePose(this.prevEstimatedRobotPose.estimatedPose);

    SmartDashboard.putString("latestResult", cam.getLatestResult().toString());

    Optional<EstimatedRobotPose> pose = photonPoseEstimator.update(cam.getLatestResult());
    if(pose.isPresent()) {
      this.prevEstimatedRobotPose = pose.get();
      return this.prevEstimatedRobotPose.targetsUsed;
    }
    return new ArrayList<PhotonTrackedTarget>();
  }
  
  public Optional<EstimatedRobotPose> getGlobalPose() {
    this.photonPoseEstimator.setReferencePose(this.prevEstimatedRobotPose.estimatedPose);

    SmartDashboard.putString("latestResult", cam.getLatestResult().toString());

    Optional<EstimatedRobotPose> pose = photonPoseEstimator.update(cam.getLatestResult());
    if(pose.isPresent()) {
      this.prevEstimatedRobotPose = pose.get();
      return Optional.of(this.prevEstimatedRobotPose);
    }
    return Optional.empty();
  }

  public Optional<Transform3d> getRobotToTag(int tagID) {
    // TODO: Use one tag only / is this even required? Right now will extrapolate to transform from a tag without
    // seeing that tag.

    // Could not work out pose
    Optional<EstimatedRobotPose> globalPose = this.getGlobalPose();
    SmartDashboard.putString("globalPose", globalPose.toString());
    if(globalPose.isEmpty()) {
      return Optional.empty();
    }

    // Look for tag
    String tagText = "";
    List<AprilTag> tags = this.aprilTagFieldLayout.getTags();
    for(AprilTag tag : tags) {
      if(tag.ID == tagID) {
        tagText += "." + tag.ID;
        return Optional.of(tag.pose.minus(globalPose.get().estimatedPose));
      }
    }
    SmartDashboard.putString("tags", tagText);

    // Required tag not on field
    return Optional.empty();
  }
}
