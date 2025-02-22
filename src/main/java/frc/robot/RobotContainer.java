// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.LiftConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.LiftConstants.Height;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.Lift;
import frc.robot.commands.RunLift;
import frc.robot.subsystems.DatisLift;
import frc.robot.commands.FieldOrientedDrive;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ExampleSubsystem;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Command.InterruptionBehavior;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final DatisLift lift= new DatisLift(new SparkMax(LiftConstants.liftNeoCANID, MotorType.kBrushless), new SparkMax(LiftConstants.reversedLiftNeoCANDID, MotorType.kBrushless), new DutyCycleEncoder(LiftConstants.encoderChannel));
  private final DriveSubsystem m_driveSubsystem= new DriveSubsystem();
  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
  new CommandXboxController(OIConstants.kDriverControllerPort);
  
  private final FieldOrientedDrive fieldOrientedDrive= new FieldOrientedDrive(m_driveSubsystem, m_driverController);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    // m_driveSubsystem.setDefaultCommand(fieldOrientedDrive);
    // m_driveSubsystem.drive(m_driverController.getLeftX(), m_driverController.getLeftY(), 0, false);
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    
    //delete once done testing
    // m_driverController.rightBumper().whileTrue(new RunLift(lift, true));
    // m_driverController.leftBumper().whileTrue(new RunLift(lift, false));
    

    
    m_driverController.rightBumper().onTrue(new Lift(lift, Height.Ground));
    m_driverController.leftBumper().onTrue(new Lift(lift, Height.L4));
    //I create keybinds for all the other heights later
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }
}
