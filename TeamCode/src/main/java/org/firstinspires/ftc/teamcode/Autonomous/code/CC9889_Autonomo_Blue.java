package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import static com.qualcomm.robotcore.util.Range.clip;

/**
 * Created by Jin on 9/30/2016. #WeGonRideWeGonWin #ObieDidHarambe
 */
@Autonomous(name="AutoBlue", group="Blue")
public class CC9889_Autonomo_Blue extends LinearOpMode {


    //Flywheel Motors
    DcMotor rightShoot;
    DcMotor leftShoot;

    //Drivetrain Motors
    DcMotor RDrive1;
    DcMotor RDrive2;
    DcMotor LDrive1;
    DcMotor LDrive2;

    //Intake Motor
    DcMotor Intake;

    //Beacon-pushing Servos
    Servo RightBumper;
    Servo LeftBumper;

    //Sensors
    OpticalDistanceSensor RWhiteLine;
    OpticalDistanceSensor LWhiteLine;
    ColorSensor Color;
    GyroSensor Gyro;

    //DcMotor Encoders
    static final double EncoderCounts=1120;
    static final double WheelDiameter=4.0;
    static final double CountsPerInch=EncoderCounts/(WheelDiameter*3.1415926535897932384626433832795);
    static final double DriveSpeed=0.7;
    static final double TurnSpeed=0.1;

    private ElapsedTime period  = new ElapsedTime();
    private ElapsedTime runtime=new ElapsedTime();

    //Flywheel
    double Flywheelnum = 0.0;


    @Override
    public void runOpMode () {

        setup();

        waitForTick(50);

        waitForStart();

        encoderDrive(0.5, 6.5, 0, 1000);
        FindWhiteTape(0.7, true);
        BumperControl(false);
        HitButton(true);

        BumperControl(true);
        encoderDrive(0.5, -12, 12, 1000);

        FindWhiteTape(0.7, true);
        encoderDrive(0.1, -3, -3, 1000);
        BumperControl(false);
        HitButton(true);

        super.stop();
    }

    //Functions
    public void setup(){

    }

  //Encoders
    public void encoderDrive (double speed, double leftInches, double rightInches, long timeoutS){

        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {
            // Determine new target position, and pass to motor controller

            newLeftTarget = LDrive1.getCurrentPosition() + (int)(leftInches * CountsPerInch);
            newRightTarget = RDrive1.getCurrentPosition() + (int)(rightInches * CountsPerInch);

            LDrive1.setTargetPosition(newLeftTarget);
            RDrive1.setTargetPosition(newRightTarget);



            // Turn On RUN_TO_POSITION
            LDrive1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            RDrive1.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();

            LDrive1.setPower(Math.abs(speed));
            RDrive1.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() && (runtime.seconds() < timeoutS) && (LDrive1.isBusy() && RDrive1.isBusy())) {
                LDrive1.setPower(speed);
                RDrive1.setPower(speed);

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d", LDrive1.getCurrentPosition(), RDrive1.getCurrentPosition());
                telemetry.update();

                waitForTick(25);
            }

            // Stop all motion;
            LDrive1.setPower(0);
            RDrive1.setPower(0);

            sleep(timeoutS);   // optional pause after each move

            LDrive1.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);
            RDrive1.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);

        }

    }

    //Go to white line
    public void FindWhiteTape(double speed, boolean color){
        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        while (opModeIsActive() && RWhiteLine.getRawLightDetected() < 1.5 && LWhiteLine.getRawLightDetected() < 1.5){
            Drivetrain(speed,speed);            waitForTick(10);
        }
        Drivetrain(0,0);

        sleep(500);

        encoderDrive(0.2, 4, 4, 100);

        if (opModeIsActive() && color == true){
            while (opModeIsActive() && LWhiteLine.getRawLightDetected() < 1.5){
                Drivetrain(0.5,-0.5);
                waitForTick(10);
            }
        }else {
            while (opModeIsActive() && RWhiteLine.getRawLightDetected() < 1.5){
                Drivetrain(-0.5,0.5);
                waitForTick(10);
            }
        }
        Drivetrain(0,0);
        sleep(500);

        LDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RDrive1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        LDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        RDrive2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    //Follow Line and Press Button
    public void HitButton(boolean color){
        encoderDrive(0.1, 2, 2, 100);
        Drivetrain(0,0);

        if(opModeIsActive() && color == true){
            Drivetrain(0.1,0.1);

            sleep(1050);

            if(Color.red() < Color.blue()){
                LeftBumper.setPosition(0.8);
            }else {
                RightBumper.setPosition(0.2);
            }

            sleep(1000);

            Drivetrain(0,0);

            encoderDrive(0.2, -4, -4, 100);
        }
    }

    //Controller for all bumper actions
    public void BumperControl(boolean updown){
        if(updown == true){
            LeftBumper.setPosition(0.8);
            RightBumper.setPosition(0.2);
        }else if(opModeIsActive() && updown == false){
            LeftBumper.setPosition(0.12);
            RightBumper.setPosition(0.88);
        }
    }

    //Flywheel Control
    public void Flywheel(int Position){
        if(Position == 0){
            Flywheelnum = 0.0;
            leftShoot.setPower(Flywheelnum);
            rightShoot.setPower(Flywheelnum);
        }else {
            Flywheelnum = 1.0;
            leftShoot.setPower(Flywheelnum);
            rightShoot.setPower(Flywheelnum);
            sleep(500);
            if(Position == 1){
                Flywheelnum = 0.34;
            }else if(Position == 2){
                Flywheelnum = 0.60;
            }else if(Position == 4){
                Flywheelnum = 0.8;
            }else {
                Flywheelnum = 0.0;
            }
            leftShoot.setPower(Flywheelnum);
            rightShoot.setPower(Flywheelnum);
        }
    }

    public void waitForTick(long periodMs) {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0) {
            try {
                Thread.sleep(remaining);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Reset the cycle clock for the next pass.
        period.reset();
    }

    //Drive
    public void Drivetrain(double left, double right){
        LDrive1.setPower(left);
        LDrive2.setPower(left);
        RDrive1.setPower(right);
        RDrive2.setPower(right);
    }
}
