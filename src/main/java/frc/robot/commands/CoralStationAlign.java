// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import java.lang.reflect.Field;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.FieldOrientedDriveConstants;
import frc.robot.Constants.TestingConstants;
import frc.robot.subsystems.DriveSubsystem;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class CoralStationAlign extends Command {
  private DriveSubsystem driveSubsystem;
  private PIDController bearingPIDController;
  private CommandXboxController controller;
  /** Creates a new CoralStationAlign.
   */
  public CoralStationAlign(DriveSubsystem driveSubsystem, CommandXboxController controller) {
    this.driveSubsystem=driveSubsystem;
    this.controller=controller;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    //remember that gyro is flipped
   bearingPIDController=new PIDController(FieldOrientedDriveConstants.kFODP, FieldOrientedDriveConstants.kFODI, FieldOrientedDriveConstants.kFODD);
   //coral station bearing is at 240 to 0, where 0 is forwards for the robot. 
   bearingPIDController.setSetpoint(4/3*Math.PI);
   bearingPIDController.setTolerance(FieldOrientedDriveConstants.bearingTolerance);
   bearingPIDController.enableContinuousInput(0, 2*Math.PI);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double bearing = Math.toRadians(driveSubsystem.getGyroAngle());
    double joystickMoveBearing = Math.atan2(controller.getLeftX(), -controller.getLeftY());
    double joystickMoveMagnitude = Math.hypot(controller.getLeftX(), controller.getLeftY());
    double xSpeed= joystickMoveMagnitude * Math.cos(joystickMoveBearing) * TestingConstants.maximumSpeedReduced;
    double ySpeed= joystickMoveMagnitude * Math.sin(joystickMoveBearing) * TestingConstants.maximumSpeedReduced;
    driveSubsystem.drive(xSpeed, -ySpeed, bearingPIDController.calculate(bearing), false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
