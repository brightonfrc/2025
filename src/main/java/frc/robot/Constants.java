// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class ArmConstants{
    public static final int armCANID=13;
    public static final int armEncoderPort=0;
    //its all in degrees, remember to convert
    public static final double[] desiredArmAngle= new double[]{
      //do note that ground height isn't actually at 0, because arm has minimum height
      0,//Ground intake (Not sure what angle this needs to be)
      -75,//L1
      70,//L2 and L3
      45 //L4 
    };
    public static final double maxPower=0.2;
    public static final double kPArm=0.1;
    public static final double kIArm=0;
    public static final double kDArm=0;
    //1 degree tolerance
    public static final double angleTolerance=Math.PI/180;
  }
  public static class LiftConstants{
    //for some reason at 0.04 the robot goes insane
    public static final double maximumPowerChange=0.05;
    public static final double maximumTilt=10;
    public static final double angleAtPeakHeight=87.6;
    public static final int encoderChannel=0;
    public static final int liftNeoCANID=11;
    public static final int reversedLiftNeoCANDID=12;
    //to ensure the arm doesn't tear itself apart. 
    public static final double maxPower=0.9;

    public static final double[] desiredHeight= new double[]{
      //do note that ground height isn't actually at 0, because arm has minimum height
      0.30,//ground intake 
      0.46,//L1
      0.81,//L2
      1.21,//L3
      1.4 //L4 (Officially its 6-feet up, but the at the end can also move)
    };
    

    public static enum Height{
      Ground,
      L1,
      L2,
      L3,
      L4,
      Stow
    }
    //arm length is 29 inches?
    public static final double armLength=27.4*0.0254;
    public static final double kPLift=0.6;
    public static final double kILift=0.0;
    public static final double kDLift=0;
    public static final double angleTolerance=Math.PI/180;
    //remember to adjust as more components are added
    public static final double kWeightMomentOffsetFactor=0.0246;

    
  }
  public static class AccelerationLimiterConstants{
    public static double maximumAcceleration=0.02;
    public static double maximumDeceleration=0.02;
  }
  public static class TestingConstants{
    public static final double maximumSpeed=0.40;
    public static final double maximumRotationSpeed=0.01;
    public static final double maximumSpeedReduced=0.10;
    public static final double maximumRotationSpeedRobotOriented=0.005;
  }

  public static class GameSetup {
    public static final double startAngleOffset = 0; // Degrees
    public static final boolean isFieldRelative = false; // true: field relative; false: robot relative
    public static final String pathPlannerAutoStrategy = "Speaker 1 Note Right";
  }

  public static class CANIds {

    public static final int kLeftShooterMotor = 11;
    public static final int kRightShooterMotor = 10;
    public static final int kIntakeMotor = 9;
    // public static final int kLiftMotor = 12;
    }
  public static final class FieldOrientedDriveConstants{
    public static final double kFODP = 0.25;
    public static final double kFODI = 0.3;
    public static final double kFODD = 0.0;

    //Maximum rotation speed
    public static final double rotationScalar = Math.PI;

    public static final double bearingTolerance = 0.5;

    public static final double moveJoystickDeadzone=0.02;
  }
  public static final class DriveConstants {
    // Driving Parameters - Note that these are not the maximum capable speeds of
    // the robot, rather the allowed maximum speeds
    public static final double kMaxSpeedMetersPerSecond = 4.8;
    public static final double kMaxAngularSpeed = 2 * Math.PI; // radians per second

    public static final double kDirectionSlewRate = 1.2; // radians per second
    public static final double kMagnitudeSlewRate = 1.8; // percent per second (1 = 100%)
    public static final double kRotationalSlewRate = 2.0; // percent per second (1 = 100%)

    // Chassis configuration
    public static final double kTrackWidth = Units.inchesToMeters(14) + 90;
    // Distance between centers of right and left wheels on robot
    public static final double kWheelBase = Units.inchesToMeters(14) + 90;
    // Distance between front and back wheels on robot
    public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

    // Angular offsets of the modules relative to the chassis in radians
    public static final double kFrontLeftChassisAngularOffset = -Math.PI / 2;
    public static final double kFrontRightChassisAngularOffset = 0;
    public static final double kBackLeftChassisAngularOffset = Math.PI;
    public static final double kBackRightChassisAngularOffset = Math.PI / 2;

    // SPARK MAX CAN IDs
    public static final int kFrontLeftDrivingCanId = 1;
    public static final int kFrontLeftTurningCanId = 2;
    public static final int kFrontRightDrivingCanId = 7;
    public static final int kFrontRightTurningCanId = 8;
    public static final int kRearLeftDrivingCanId = 3;
    public static final int kRearLeftTurningCanId = 4;
    public static final int kRearRightDrivingCanId = 5;
    public static final int kRearRightTurningCanId = 6;

    public static final boolean kGyroReversed = false;
  }

  public static final class ModuleConstants {
    // The MAXSwerve module can be configured with one of three pinion gears: 12T, 13T, or 14T.
    // This changes the drive speed of the module (a pinion gear with more teeth will result in a
    // robot that drives faster).
    public static final int kDrivingMotorPinionTeeth = 13;

    // Invert the turning encoder, since the output shaft rotates in the opposite direction of
    // the steering motor in the MAXSwerve Module.
    public static final boolean kTurningEncoderInverted = true;

    // Calculations required for driving motor conversion factors and feed forward
    public static final double kDrivingMotorFreeSpeedRps = NeoMotorConstants.kFreeSpeedRpm / 60;
    public static final double kWheelDiameterMeters = 0.0762;
    public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
    // 45 teeth on the wheel's bevel gear, 22 teeth on the first-stage spur gear, 15 teeth on the bevel pinion
    public static final double kDrivingMotorReduction = (45.0 * 22) / (kDrivingMotorPinionTeeth * 15);
    public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters)
        / kDrivingMotorReduction;

    public static final double kDrivingEncoderPositionFactor = (kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction; // meters
    public static final double kDrivingEncoderVelocityFactor = ((kWheelDiameterMeters * Math.PI)
        / kDrivingMotorReduction) / 60.0; // meters per second

    public static final double kTurningEncoderPositionFactor = (2 * Math.PI); // radians
    public static final double kTurningEncoderVelocityFactor = (2 * Math.PI) / 60.0; // radians per second

    public static final double kTurningEncoderPositionPIDMinInput = 0; // radians
    public static final double kTurningEncoderPositionPIDMaxInput = kTurningEncoderPositionFactor; // radians

    public static final double kDrivingP = 0.04;
    public static final double kDrivingI = 0;
    public static final double kDrivingD = 0;
    public static final double kDrivingFF = 1 / kDriveWheelFreeSpeedRps;
    public static final double kDrivingMinOutput = -1;
    public static final double kDrivingMaxOutput = 1;

    public static final double kTurningP = 1;
    public static final double kTurningI = 0;
    public static final double kTurningD = 0;
    public static final double kTurningFF = 0;
    public static final double kTurningMinOutput = -1;
    public static final double kTurningMaxOutput = 1;

    public static final IdleMode kDrivingMotorIdleMode = IdleMode.kBrake;
    public static final IdleMode kTurningMotorIdleMode = IdleMode.kBrake;

    public static final int kDrivingMotorCurrentLimit = 50; // amps
    public static final int kTurningMotorCurrentLimit = 20; // amps
  }

  public static final class OIConstants {
    public static final int kDriverControllerPort = 0;
    public static final double kDriveDeadband = 0.1;
  }

  public static final class AutoConstants {
    public static final double kMaxSpeedMetersPerSecond = 3;
    public static final double kMaxAccelerationMetersPerSecondSquared = 3;
    public static final double kMaxAngularSpeedRadiansPerSecond = Math.PI;
    public static final double kMaxAngularSpeedRadiansPerSecondSquared = Math.PI;

    public static final double kPXController = 1;
    public static final double kPYController = 1;
    public static final double kPThetaController = 1;

    // Constraint for the motion profiled robot angle controller
    public static final TrapezoidProfile.Constraints kThetaControllerConstraints = new TrapezoidProfile.Constraints(
        kMaxAngularSpeedRadiansPerSecond, kMaxAngularSpeedRadiansPerSecondSquared);

    public static final long kShooterDurationMillis = 5000; // 3s without intake then 2 seconds with it
    public static long kIntakeDurationMillis = 2000; // 2 seconds
  }

  public static final class NeoMotorConstants {
    public static final double kFreeSpeedRpm = 5676;
  }

  // Don't think we need this, but just in case
  // public static final class LiftConstants{
  //   public static final double kP = 0.1;
  //   public static final double kI = 0;
  //   public static final double kD = 0;
  //   public static final double kTolerance = 0.1;

  //   public static final double kSetpointClimb = 1;
  //   public static final double kSetpointDeclimb = 0;

  // }
}