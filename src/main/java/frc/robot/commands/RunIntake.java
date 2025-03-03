// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.LiftConstants;
import frc.robot.Constants.LiftConstants.Height;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DatisLift;
import frc.robot.subsystems.Intake;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class RunIntake extends Command {
  private Intake intake;
  private Boolean in;
  private DatisLift lift;
  private Arm arm;
  private Boolean l4Outake;
  /** Creates a new RunIntake. 
   * the boolean IN should be true if you are intaking coral but outtaking coral
  */
  public RunIntake(Intake intake, Boolean in, DatisLift lift, Arm arm) {
    this.intake=intake;
    this.in=in;
    // Use addRequirements() here to declare subsystem dependencies.
    this.lift=lift;
    this.arm=arm;
    addRequirements(intake, lift, arm);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if ((in==false) && (lift.getHeight()==Height.L4)){
      l4Outake=true;
    }
    else{
      l4Outake=false;
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (l4Outake){  
      intake.outtake();
      lift.setPower(LiftConstants.liftFallingPower);
      arm.setPower(ArmConstants.armFallingPower);
    }else{
      SmartDashboard.putBoolean("command active", true);
      if(in){
        
        intake.intake();
      }
      else{
        intake.outtake();
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stop();
    lift.setPower(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
