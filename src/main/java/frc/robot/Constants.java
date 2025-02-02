// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.system.plant.DCMotor;
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
  public static class PathPlannerConstants{
    public static final DCMotor m_dcmotor= DCMotor.getNEO(1);
    public static final ModuleConfig moduleConfig= new ModuleConfig(ModuleConstants.kWheelDiameterMeters/2, DriveConstants.kMaxSpeedMetersPerSecond, 1.0, m_dcmotor, 80, 1);
    //Each swerve module is 1.720kg (using neo). So 4 of them are 0.81176m from center of robot
    //Sum of MOI Of 4 modules is 4.5336 KGM^2
    //The rest of chassis is 6.12 kg
    //Plug into online calculator to get 0.4463 KGM^2
    public static final double momentOfInertia=0.49799;
    public static final RobotConfig config= new RobotConfig(13, momentOfInertia, moduleConfig, new Translation2d[]
    {
      new Translation2d(DriveConstants.kWheelBase / 2, DriveConstants.kTrackWidth / 2),
      new Translation2d(DriveConstants.kWheelBase / 2, -DriveConstants.kTrackWidth / 2),
      new Translation2d(-DriveConstants.kWheelBase / 2, DriveConstants.kTrackWidth / 2),
      new Translation2d(-DriveConstants.kWheelBase / 2, -DriveConstants.kTrackWidth / 2)
    }
    );
  }
  public static class AccelerationLimiterConstants{
    public static double maximumAcceleration=0.02;
    public static double maximumDeceleration=0.02;
  }
  public static class OperatorConstants{
    public static final int kDriverControllerPort=0;
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
    //track width (left of left wheel to right of right wheel) is 60cm. Single wheel width is 2.6cm ish?
    public static final double kTrackWidth = 0.574;
    // Distance between centers of right and left wheels on robot

    //track length (front of left wheel to back of left wheel) is 63.1-5.7
    public static final double kWheelBase = 0.574;
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