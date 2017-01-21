package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static com.qualcomm.robotcore.util.Range.clip;

/**
 * Created by Jin on 9/30/2016. #WeGonRideWeGonWin #ObieDidHarambe
 */
@Autonomous(name="AltAutoBlue", group="Blue")
public class CC9889_AltAutoBlue extends LinearOpMode {


    /* Declare OpMode members. */
    AutoHardware9889 robot           = new AutoHardware9889();

    int randomnumberthatweneedforsomething = 1;
    boolean breakout = false;

    @Override
    public void runOpMode () {
        robot.init(hardwareMap);

        robot.resetEncoders();

        /*while (!isStopRequested() && robot.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }*/

        while (breakout == false) {
            telemetry.clearAll();
            if (gamepad1.dpad_up) {
                randomnumberthatweneedforsomething = 1;
                telemetry.addData("Autonomous 1", "= Shoot and Park on Center");
            } else if(gamepad1.dpad_right){
                randomnumberthatweneedforsomething = 2;
                telemetry.addData("Autonomous 2", "= 2 Beacon");
            }else if (gamepad1.dpad_down) {
                randomnumberthatweneedforsomething = 3;
                telemetry.addData("Autonomous 3","= 1  Beacon and Ramp");
            }else if(gamepad1.dpad_left) {
                randomnumberthatweneedforsomething = 4;
                telemetry.addData("Autonomous 4", "= 1 Beacon and Hit Cap Ball");
            }else if (gamepad1.a){
                breakout = true;
            }
            telemetry.addData("Please Select an ", "Autonomous Mode");
            //telemetry.addData(">", "Gyro Calibrated. ¯\\_(ツ)_/¯");
            telemetry.update();
        }
        telemetry.clearAll();
        telemetry.addData("Auton", " Selected");
        updateData();

        waitForStart();

        telemetry.addData("Runnig Auton", " ");

        robot.resetEncoders();
        robot.gyro.resetZAxisIntegrator();
        updateData();

        sleep(100);



        if (randomnumberthatweneedforsomething == 1){//Shoot and Park on Center Auton
            //Wait for partner to hit beacon
            sleep(15000);

            if (opModeIsActive()){
            //Start Flywheel
            robot.Flywheel(true);
            }

            //Drive Straight For 35 inches
            while (opModeIsActive() && robot.getLeftEncoderinInches() > -35 && robot.getRightEncoderinInches() > -35){
                robot.Drivetrain(-0.6, -0.6);
                updateData();
                robot.waitForTick(50);
            }
            robot.STOP();

            robot.resetEncoders();

            //Shoot particles
            if(opModeIsActive()){
                sleep(500);
                robot.IntakeServo.setPower(-1.0);
                robot.Intake.setPower(0.8);
                sleep(3000);
                robot.IntakeServo.setPower(0.0);
                robot.Intake.setPower(0.0);
                robot.Flywheel(false);
            }

            //Park
            while (opModeIsActive() && robot.getLeftEncoderinInches() > -35 && robot.getRightEncoderinInches() > -35){
                robot.Drivetrain(-0.6, -0.6);
                updateData();
                robot.waitForTick(50);
            }
            robot.STOP();


        }else {///////////////////////Base One Beacon///////////////////////

            //Drive Straight For 22 inches
            while (opModeIsActive() && robot.getLeftEncoderinInches() > -20 && robot.getRightEncoderinInches() > -20){
                robot.Drivetrain(-0.6, -0.6);
                updateData();
                robot.waitForTick(50);
            }
            robot.STOP();

            //Turn to the goal
            while (opModeIsActive() && robot.getGyro() < 20){
                robot.Drivetrain(-0.1, 0.1);
                updateData();
                robot.waitForTick(25);
            }

            robot.STOP();

            //Shoot particles
            if(opModeIsActive()){
                robot.Flywheel(true);
                sleep(1200);
                robot.IntakeServo.setPower(-1.0);
                robot.Intake.setPower(0.8);
                sleep(3000);
                robot.IntakeServo.setPower(0.0);
                robot.Intake.setPower(0.0);
                robot.Flywheel(false);
            }


            while (opModeIsActive() && robot.getGyro() > -47){
                robot.Drivetrain(0.1, -0.1);
                updateData();
                robot.waitForTick(25);
            }

            robot.STOP();

            while (opModeIsActive() && robot.BackODS.getRawLightDetected() < 0.6){
                robot.Drivetrain(-0.9, -0.9);
                updateData();
                robot.waitForTick(50);
            }

            robot.STOP();

            while (opModeIsActive() && robot.BackODS.getRawLightDetected() < 0.6){
                robot.Drivetrain(0.1, 0.1);
                updateData();
                robot.waitForTick(50);
            }
            robot.STOP();

            while (opModeIsActive() && robot.FrontODS.getRawLightDetected() < 1.0){
                robot.Drivetrain(0.15, -0.15);
                updateData();
                robot.waitForTick(50);
            }

            robot.STOP();

            robot.BumperControl(true);

            //Drive to the beacon
            robot.Drivetrain(-0.2, -0.2);
            sleep(200);
            HitButton(true);

            robot.resetEncoders();

            while (opModeIsActive() && robot.getLeftEncoderinInches() < 5 && robot.getRightEncoderinInches() < 5){
                robot.Drivetrain(0.4, 0.4);
                updateData();
                robot.waitForTick(50);
            }
            robot.STOP();

            robot.BumperControl(false);

            ////////////////////////////////////////////////////////
            /////       Auton Picker                        ////////
            ////////////////////////////////////////////////////////

            if (randomnumberthatweneedforsomething == 2){
                //2 BEACON AUTONOMOUS
                //Turn to the Beacon
                while (opModeIsActive() && robot.getGyro() < 0){
                    robot.Drivetrain(-0.1, 0.1);
                    updateData();
                    robot.waitForTick(25);
                }

                robot.STOP();

                robot.Drivetrain(-1.0, -1.0);
                sleep(1000);

                while (opModeIsActive() && robot.BackODS.getRawLightDetected() < 1.0){
                    if(robot.getGyro() < -1){
                        robot.Drivetrain(0.6, 0.7);
                    }else if(robot.getGyro() > 1){
                        robot.Drivetrain(0.7, 0.6);
                    }else if (robot.getGyro() == 0){
                        robot.Drivetrain(-0.7, -0.7);
                    }
                    updateData();
                    robot.waitForTick(50);
                }

                robot.STOP();

                while (opModeIsActive() && robot.BackODS.getRawLightDetected() < 1.0){
                    robot.Drivetrain(0.1, 0.1);
                    updateData();
                    robot.waitForTick(50);
                }
                robot.STOP();

                while (opModeIsActive() && robot.FrontODS.getRawLightDetected() < 0.6){
                    robot.Drivetrain(0.2, -0.2);
                    updateData();
                    robot.waitForTick(50);
                }

                robot.STOP();

                robot.BumperControl(false);
            }else if (randomnumberthatweneedforsomething == 3){//Park on Ramp
                //Turn the ramp
                while (opModeIsActive() && robot.getGyro() < 0){
                    robot.Drivetrain(-0.1, 0.1);
                    updateData();
                    robot.waitForTick(25);
                }

                robot.STOP();

                robot.Drivetrain(0.5, 0.5);
                sleep(2000);

                robot.STOP();


            }else if (randomnumberthatweneedforsomething == 4){//Cap Ball Park

                robot.STOP();
                robot.resetEncoders();

                //Drive Backward
                while (opModeIsActive() && robot.getLeftEncoderinInches() < 30 && robot.getRightEncoderinInches() < 30){
                    robot.Drivetrain(-0.4, -0.4);
                    updateData();
                    robot.waitForTick(50);
                }
                robot.STOP();


            }else {
                telemetry.addData("Invald", " Autonomous Mode");
                telemetry.update();
                sleep(15000);
            }
        }

        super.stop();
    }

    //Go to white line

    public void FindWhiteTape(double speed, boolean color){
        robot.Drivetrain(Math.abs(speed), -Math.abs(speed));
        sleep(1500);

        while (opModeIsActive() && robot.BackODS.getRawLightDetected() < 1.8){
            sleep(4);
            robot.waitForTick(50);
        }

        robot.STOP();

        //The robot lines itself up with the white line.
        robot.Drivetrain(0.0, 0.4);
        while (opModeIsActive() && robot.FrontODS.getRawLightDetected() < 1.5){
            sleep(4);
            robot.waitForTick(50);
        }

        robot.STOP();

    }

    //Follow Line and Press Button

    public void HitButton(boolean color){
        //Here the robot decides which beacon button to press.
        if(color == true){//Go for red
            if (robot.Color.red() > robot.Color.blue()){
                robot.RightBumper.setPosition(0.0);
            }else {
                robot.LeftBumper.setPosition(1.0);
            }
        }else {//Go for blue
            if (robot.Color.red() < robot.Color.blue()){
                robot.RightBumper.setPosition(0.0);
            }else {
                robot.LeftBumper.setPosition(1.0);
            }
        }
        sleep(700);

        robot.STOP();
    }

    public void updateData(){
        telemetry.addData("Right Speed", robot.RDrive1.getPower());
        telemetry.addData("Left Speed", robot.LDrive1.getPower());
        telemetry.addData("Right Encoder", robot.getRightEncoder());
        telemetry.addData("Left Encoder", robot.getLeftEncoder());
        telemetry.addData("Right Encoder pos", robot.RDrive1.getCurrentPosition());
        telemetry.addData("Left Encoder pos", robot.LDrive1.getCurrentPosition());
        telemetry.addData("Right Encoder in Inches", robot.getRightEncoderinInches());
        telemetry.addData("Left Encoder in Inches", robot.getLeftEncoderinInches());
        telemetry.addData("Gyro Z-axis", robot.gyro.getIntegratedZValue());
        telemetry.addData("Left ODS", robot.BackODS.getRawLightDetected());
        telemetry.addData("Right ODS", robot.FrontODS.getRawLightDetected());

        telemetry.update();
    }
}