// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.AutonomousNavConstants;
import frc.robot.Constants.FieldOrientedDriveConstants;
import frc.robot.subsystems.DriveSubsystem;


/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class MoveToPoint extends Command {
  private DriveSubsystem driveSubsystem;
  private PIDController xController;
  private PIDController yController;
  private PIDController rotController;
  private Pose2d goal; 
  /** Creates a new MoveToPoint. */
  public MoveToPoint(DriveSubsystem driveSubsystem, Pose2d goal) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.driveSubsystem=driveSubsystem;
    addRequirements(driveSubsystem);
    this.goal=goal;
  }


  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    xController=new PIDController(AutonomousNavConstants.translationkP,AutonomousNavConstants.translationkI, AutonomousNavConstants.translationkD);
    xController.setSetpoint(goal.getX());
    xController.setTolerance(AutonomousNavConstants.translationTolerance);
    yController=new PIDController(AutonomousNavConstants.translationkP,AutonomousNavConstants.translationkI, AutonomousNavConstants.translationkD);
    yController.setSetpoint(goal.getY());
    yController.setTolerance(AutonomousNavConstants.translationTolerance);
    rotController=new PIDController(FieldOrientedDriveConstants.kFODP, FieldOrientedDriveConstants.kFODI, FieldOrientedDriveConstants.kFODD);
    rotController.setTolerance(AutonomousNavConstants.rotationTolerance);
    rotController.enableContinuousInput(0, 2*Math.PI);

  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double robotBearing = driveSubsystem.getGyroAngle();
    if (robotBearing<0){
      //converting to within range 0 to 360 degrees
      robotBearing+=360;
    }
    SmartDashboard.putNumber("Pose/x", driveSubsystem.getPose().getX());
    SmartDashboard.putNumber("Pose/y", driveSubsystem.getPose().getY());
    SmartDashboard.putNumber("Pose/rot", robotBearing);
    
    double rotSpeed=rotController.calculate(robotBearing);
    double xSpeed=xController.calculate(driveSubsystem.getPose().getX());
    double ySpeed=yController.calculate(driveSubsystem.getPose().getY());
    SmartDashboard.putNumber("Speed/x", xSpeed);
    SmartDashboard.putNumber("Speed/y", ySpeed);
    SmartDashboard.putNumber("Speed/rot", ySpeed);
    driveSubsystem.drive(xSpeed, ySpeed, rotSpeed, false);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSubsystem.drive(0, 0, 0, false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (xController.atSetpoint()&&yController.atSetpoint()&&rotController.atSetpoint());
  }
}
