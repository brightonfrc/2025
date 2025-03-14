// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DatisLift;
import frc.robot.Constants.AngleLimitConstants;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.LiftConstants;
import frc.robot.Constants.LiftConstants.Height;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class JankLift extends Command {
  private DatisLift lift;
  private Arm arm;
  private double angleRequired;
  private double armAngleRequired;
  private PIDController liftController;
  private PIDController armController;
  private double previousPower;
  private Boolean emergencyStop;
  private Height height;
  private PIDController armResetController;
  private PIDController liftResetController;
  /** Creates a new Lift. */
  public JankLift(DatisLift lift, Arm arm, Height heightDesired) {
    this.arm=arm;
    this.lift=lift;
    switch(heightDesired){
      case Ground:
        angleRequired=LiftConstants.desiredLiftAngle[0];
        armAngleRequired=ArmConstants.desiredArmAngle[0];
        break;
      case L1:
        angleRequired=LiftConstants.desiredLiftAngle[1];
        armAngleRequired=ArmConstants.desiredArmAngle[1];
        break;
      case L2:
        angleRequired=LiftConstants.desiredLiftAngle[2];
        armAngleRequired=ArmConstants.desiredArmAngle[2];
        break;
      case L3:
        angleRequired=LiftConstants.desiredLiftAngle[3];
        armAngleRequired=ArmConstants.desiredArmAngle[3];
        break;
      case L4:
        angleRequired=LiftConstants.desiredLiftAngle[4];
        armAngleRequired=ArmConstants.desiredArmAngle[4];
        break;
      case Algae2:
        angleRequired=LiftConstants.desiredLiftAngle[5];
        armAngleRequired=ArmConstants.desiredArmAngle[5];
        break;
      case Algae3:
        angleRequired=LiftConstants.desiredLiftAngle[6];
        armAngleRequired=ArmConstants.desiredArmAngle[6];
        break;
      case CoralStation:
        angleRequired=LiftConstants.desiredLiftAngle[7];
        armAngleRequired=ArmConstants.desiredArmAngle[7];
        break;
      case StartingConfig:
        angleRequired=LiftConstants.desiredLiftAngle[8];
        armAngleRequired=ArmConstants.desiredArmAngle[8];
        break;
      case HangStart:
        angleRequired=LiftConstants.desiredLiftAngle[9];
        armAngleRequired=ArmConstants.desiredArmAngle[9];
        break;
      // case HangEnd:
      //   angleRequired=LiftConstants.desiredLiftAngle[10];
      //   armAngleRequired=ArmConstants.desiredArmAngle[10];
      //   break;
      case InternalStow:
        angleRequired = LiftConstants.desiredLiftAngle[10];
        armAngleRequired = ArmConstants.desiredArmAngle[10];
        break;
    }
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(lift);
    armAngleRequired=Math.toRadians(armAngleRequired);
    height=heightDesired;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    liftController= new PIDController(LiftConstants.kPLift, LiftConstants.kILift, LiftConstants.kDLift);
    liftController.setTolerance(LiftConstants.angleTolerance);
    liftController.setSetpoint(angleRequired);
    previousPower=0;
    armController= new PIDController(ArmConstants.kPArm, ArmConstants.kIArm, ArmConstants.kDArm);
    armController.reset();
    armController.setTolerance(ArmConstants.angleTolerance);
    //remember to edit this as needed
    armController.setSetpoint(armAngleRequired);
    emergencyStop=false;
    lift.setHeight(height);

    liftResetController= new PIDController(0, 0.01, 0);
    liftResetController.setSetpoint(30);
    armResetController= new PIDController(0, 0.01, 0);
    armResetController.setSetpoint(0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // SmartDashboard.putNumber("Angle required/Lift", Math.toDegrees(angleRequired));
    SmartDashboard.putNumber("Angle required/Arm", Math.toDegrees(armAngleRequired));

    double currentAngle=Math.toRadians(lift.getLiftAngle());
    double desiredPower=liftController.calculate(currentAngle);
    // if (Math.abs(desiredPower-previousPower)>LiftConstants.maximumPowerChange){
    //   //controller demands excessive power change
    //   if (desiredPower<0){
    //     //reducing power
    //     desiredPower=previousPower-LiftConstants.maximumPowerChange;
    //   }
    //   else{
    //     desiredPower=previousPower+LiftConstants.maximumPowerChange;
    //   }
    // }
    desiredPower+=LiftConstants.kWeightMomentOffsetFactor*Math.cos(currentAngle);
    // SmartDashboard.putNumber("Power/Lift", desiredPower);
    // previousPower=desiredPower;
    if ((currentAngle>Math.toRadians(AngleLimitConstants.maxArmAngle))
        ||(currentAngle<Math.toRadians(AngleLimitConstants.minArmAngle))){
        //reset arm to angle 0
        lift.setPower(armResetController.calculate(currentAngle));     
      }
      else{
        lift.setPower(desiredPower);
      }
    // SmartDashboard.putBoolean("Command active", !liftController.atSetpoint());

    if (height!=Height.StartingConfig) { 
      double currentArmAngle=Math.toRadians(arm.getArmAngle());
      double desiredArmPower=armController.calculate(currentAngle+currentArmAngle);
      desiredArmPower+=ArmConstants.kWeightMomentOffsetFactor*Math.cos(Math.toRadians(currentArmAngle+currentAngle));
      SmartDashboard.putNumber("Angle/Arm", Math.toDegrees(currentAngle+currentArmAngle));
      SmartDashboard.putNumber("Power/Arm", desiredArmPower);
      // arm.setPower(desiredArmPower+ArmConstants.kWeightMomentOffsetFactor*Math.cos(Math.toRadians(currentArmAngle+currentAngle)));
      
      //emergency end command if lift or arm angle outside of expected range
      // if ((currentAngle>Math.toRadians(AngleLimitConstants.maxLiftAngle))
      // ||(currentAngle<Math.toRadians(AngleLimitConstants.minLiftAngle))
      // ||(currentArmAngle>Math.toRadians(AngleLimitConstants.maxArmAngle))
      // ||(currentArmAngle<Math.toRadians(AngleLimitConstants.minArmAngle))){
      if ((currentArmAngle>Math.toRadians(AngleLimitConstants.maxArmAngle))
        ||(currentArmAngle<Math.toRadians(AngleLimitConstants.minArmAngle))){
        //reset arm to angle 0
        arm.setPower(armResetController.calculate(currentArmAngle));     
      }
      else{
        arm.setPower(desiredArmPower);
      }
    }
    else if (height==Height.StartingConfig&&liftController.atSetpoint()){
      double currentArmAngle=Math.toRadians(arm.getArmAngle());
      double desiredArmPower=armController.calculate(currentAngle+currentArmAngle);
      desiredArmPower+=ArmConstants.kWeightMomentOffsetFactor*Math.cos(Math.toRadians(currentArmAngle+currentAngle));
      SmartDashboard.putNumber("Angle/Arm", Math.toDegrees(currentAngle+currentArmAngle));
      SmartDashboard.putNumber("Power/Arm", desiredArmPower);
      // arm.setPower(desiredArmPower+ArmConstants.kWeightMomentOffsetFactor*Math.cos(Math.toRadians(currentArmAngle+currentAngle)));
      arm.setPower(desiredArmPower);
      //emergency end command if lift or arm angle outside of expected range
      // if ((currentAngle>Math.toRadians(AngleLimitConstants.maxLiftAngle))
      // ||(currentAngle<Math.toRadians(AngleLimitConstants.minLiftAngle))
      // ||(currentArmAngle>Math.toRadians(AngleLimitConstants.maxArmAngle))
      // ||(currentArmAngle<Math.toRadians(AngleLimitConstants.minArmAngle))){
      //   emergencyStop=true;
      // }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    lift.setPower(0);
    arm.setPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }


}
