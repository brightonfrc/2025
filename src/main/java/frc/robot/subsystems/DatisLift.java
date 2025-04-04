// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LiftConstants;
import frc.robot.Constants.LiftConstants.Height;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SignalsConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
public class DatisLift extends SubsystemBase {
  private SparkMax liftNeo;
  private SparkMax reversedLiftNeo;
  private DutyCycleEncoder encoder;
  private Height height;
  private double previousPowerSet;

  /** Creates a new DatisLift. */
  public DatisLift(SparkMax liftNeo, SparkMax reversedLiftNeo, DutyCycleEncoder encoder) {
    this.liftNeo=liftNeo;
    this.reversedLiftNeo=reversedLiftNeo;
    this.encoder=encoder;
    //just a placeholder
    height=Height.CoralStation;
    previousPowerSet=0;
  }
  /**Method to get the current lift angle (0 being at lowest level) */
  public double getLiftAngle(){
    // SmartDashboard.putNumber("Raw Lift Angle", encoder.get()*360);
    SmartDashboard.putNumber("Lift Angle", 90-encoder.get()*360+LiftConstants.angleAtPeakHeight);
    return 90-encoder.get()*360+LiftConstants.angleAtPeakHeight;
  }
  /**Method used to set lift power ranging from -1 to 1 */
  public void setPower(double power){
    if(Math.abs(power)>LiftConstants.maxPower){
      if (power>0){
        power=LiftConstants.maxPower;
      }
      else{
        power=-LiftConstants.maxPower;
      }
    }
    liftNeo.set(power);
    reversedLiftNeo.set(-power);
    previousPowerSet=power;
  }
  /**Call this to store what the current height of the lift would be */
  public void setHeight(Height height){
    this.height=height;
  }
  public Height getHeight(){
    SmartDashboard.putString("Current Height:", height.name());
    return height;
  }
  public double getPreviousPowerSet(){
    return previousPowerSet;
  }
  // public void coaster(boolean isBraking){
  //   if(!isBraking){
  //     smconfig.idleMode(IdleMode.kCoast);
  //     smconfig.follow(11);
  //   }
  //   else{
  //     smconfig.idleMode(IdleMode.kBrake);
  //     smconfig.follow(11);
  //     smconfig.follow(11);
  //     liftNeo.
  //   }
  // }
}
