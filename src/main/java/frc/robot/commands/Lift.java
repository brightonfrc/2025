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
public class Lift extends Command {
  private DatisLift lift;
  private Arm arm;
  private double angleRequired;
  private double armAngleRequired;
  private PIDController liftController;
  private PIDController armController;
  private double previousPower;
  // private Boolean emergencyStop;
  /** Creates a new Lift. */
  public Lift(DatisLift lift, Arm arm, Height heightDesired) {
    this.arm=arm;
    this.lift=lift;
    double height=0;
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
      case Stow:
        angleRequired=LiftConstants.desiredLiftAngle[5];
        armAngleRequired=ArmConstants.desiredArmAngle[5];
    }
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(lift);
    armAngleRequired=Math.toRadians(armAngleRequired);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    liftController= new PIDController(LiftConstants.kPLift, LiftConstants.kILift, LiftConstants.kDLift);
    liftController.setTolerance(LiftConstants.angleTolerance);
    liftController.setSetpoint(angleRequired);
    previousPower=0;
    armController= new PIDController(ArmConstants.kPArm, ArmConstants.kIArm, ArmConstants.kDArm);
    armController.setTolerance(ArmConstants.angleTolerance);
    //remember to edit this as needed
    armController.setSetpoint(armAngleRequired);
    // emergencyStop=false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    SmartDashboard.putNumber("Lift Angle required", Math.toDegrees(angleRequired));
    SmartDashboard.putNumber("Arm Angle required", armAngleRequired);

    double currentAngle=Math.toRadians(lift.getLiftAngle());
    double desiredPower=liftController.calculate(currentAngle);
    //scaling power down to reduce strain on lift
    desiredPower=desiredPower*LiftConstants.maxPower;
    if (Math.abs(desiredPower-previousPower)>LiftConstants.maximumPowerChange){
      //controller demands excessive power change
      if (desiredPower<0){
        //reducing power
        desiredPower=previousPower-LiftConstants.maximumPowerChange;
      }
      else{
        desiredPower=previousPower+LiftConstants.maximumPowerChange;
      }
    }
    desiredPower+=LiftConstants.kWeightMomentOffsetFactor*Math.cos(currentAngle);
    SmartDashboard.putNumber("Power/Lift", desiredPower);
    previousPower=desiredPower;
    lift.setPower(desiredPower);
    // SmartDashboard.putBoolean("Command active", !liftController.atSetpoint());


    double currentArmAngle=Math.toRadians(arm.getArmAngle());
    double desiredArmPower=armController.calculate(currentArmAngle);
    SmartDashboard.putNumber("Power/Arm", desiredArmPower);
    // arm.setPower(desiredArmPower+ArmConstants.kWeightMomentOffsetFactor*Math.cos(Math.toRadians(currentArmAngle+currentAngle)));

    //emergency end command if lift or arm angle outside of expected range
    // if ((currentAngle>Math.toRadians(AngleLimitConstants.maxLiftAngle))
    // ||(currentAngle<Math.toRadians(AngleLimitConstants.minLiftAngle))
    // ||(currentArmAngle>Math.toRadians(AngleLimitConstants.maxArmAngle))
    // ||(currentArmAngle<Math.toRadians(AngleLimitConstants.minArmAngle))){
    //   emergencyStop=true;
    // }
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
    return liftController.atSetpoint()&&armController.atSetpoint();
    // return ((liftController.atSetpoint()&&armController.atSetpoint())||emergencyStop);
  }
}
