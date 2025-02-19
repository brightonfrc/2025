// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class Grabber extends SubsystemBase {
  private final WPI_VictorSPX motor;

  /** New Grabber */
  public Grabber(int motorID) {
    motor = new WPI_VictorSPX(motorID);
  }

  public void stopMotor() {
    motor.set(ControlMode.PercentOutput, 0);
  }

  public void letGo() {
    motor.set(ControlMode.PercentOutput, -0.5);
  }

  public void grab() {
    motor.set(ControlMode.PercentOutput, 0.5);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
