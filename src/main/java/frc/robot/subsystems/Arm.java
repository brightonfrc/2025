// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ArmConstants;
import frc.robot.Constants.LiftConstants;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.revrobotics.spark.SparkMax;

public class Arm extends SubsystemBase {
  /** Creates a new Arm. */
  private DutyCycleEncoder encoder;
  private SparkMax armMotor;

  public Arm(DutyCycleEncoder encoder, SparkMax motor) {
    this.encoder=encoder;
    armMotor=motor;
  }
  /**Gets the arm angle above or below the horizontal. So being below would create a negative value 
   * @returns arm angle in degrees
  */
  public double getArmAngle(){
    double measuredAngle= encoder.get();
    measuredAngle=measuredAngle*360;
    //in case you want to round the value
    // measuredAngle = Math.round(measuredAngle * Math.pow(10, ArmConstants.decimalPlaces))/ Math.pow(10, ArmConstants.decimalPlaces);
    double angleAboveHorizontal=measuredAngle-ArmConstants.angleAtZero;
    //because its 20 when fully stowed upwards
    if (angleAboveHorizontal<-180){
      angleAboveHorizontal+=360;
    }
    SmartDashboard.putNumber("Arm angle", angleAboveHorizontal);
    return measuredAngle;
  }
  /**Method used to set arm power ranging from -1 to 1 */
  public void setPower(double power){
    //remember that setting positive power should make the arm go up relative to horizontal.
    if(Math.abs(power)>ArmConstants.maxPower){
      if (power>0){
        power=LiftConstants.maxPower;
      }
      else{
        power=-LiftConstants.maxPower;
      }
    }
    armMotor.set(power);
  }
}
