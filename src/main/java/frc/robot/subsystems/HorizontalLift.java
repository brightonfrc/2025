// package frc.robot.subsystems;

// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.math.controller.PIDController;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkLowLevel.MotorType;

// public class HorizontalLift extends SubsystemBase {
//     private Joystick joystick;
//     private int deviceID;
//     private CANSparkMax motor;
//     private PIDController pidController;

//     public HorizontalLift(Joystick joystick, int deviceID, double p, double i, double d) {
//         this.joystick = joystick;
//         this.deviceID = deviceID;
//         this.motor = new CANSparkMax(deviceID, MotorType.kBrushless);
//         this.pidController = new PIDController(p, i, d);
//         pidController.setTolerance(0.1);
//     }

//     public void setPower(double power) {
//         motor.set(power);
//     }

//     public double getJoystickY() {
//         return joystick.getY();
//     }

//     public void stop() {
//         motor.stopMotor();
//     }

//     public double moveToHeight(double currentHeight, double targetHeight) {
//         double outputPower = pidController.calculate(currentHeight, targetHeight);
//         setPower(outputPower); 

//         if (Math.abs(targetHeight - currentHeight) < 0.01) {
//             stop();
//             return 0;
//         }
//         return targetHeight - currentHeight;
//     }

//     @Override
//     public void periodic() {}
// }
