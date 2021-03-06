package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.*;

/**
 * Created by Joshua H on 1/21/2017.
 */

@Autonomous(name="Red", group="Red")
public class CC9889_AltAutoRed extends LinearOpMode{

    private Flywheel Flywheel_Intake          = new Flywheel();
    private Drivebase Drivetrain              = new Drivebase();
    private Beacon Beacon                     = new Beacon();
    private waitForTick waitForTick           = new waitForTick();

    private ElapsedTime emergencystop = new ElapsedTime();

    private boolean emergency = true;

    private int randomnumberthatweneedforsomething = 1;

    @Override
    public void runOpMode(){
        //////////////////////////////////////////////////////////////////
        //   Note:                                                      //
        //      To see the methods called please refer below.           //
        //==============================================================//
        //    ____________________________________________________      //
        //   | Name                ==      Class                  |     //
        //   |--------------------------------------------------- |     //
        //   | Beacon              ==      Subsystem.Beacon       |     //
        //   | Flywheel_Intake     ==      Subsystems.Flywheel    |     //
        //   | Drivetrain          ==      Subsystems.Drivebas    |     //
        //   | waitForTick         ==      Subsystems.waitForTick |     //
        //   ------------------------------------------------------     //
        //////////////////////////////////////////////////////////////////

        //Init hardware
        Beacon.init(hardwareMap);
        Flywheel_Intake.init(hardwareMap);
        Drivetrain.init(hardwareMap);

        //Calibrate Gyro
        Drivetrain.CalibrateGyro();

        //Program Chooser
        while (!gamepad1.a) {
            if (gamepad1.dpad_up) {
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 1;
                telemetry.addData("Autonomous 1", "= Shoot and Park on Center");
                telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
            } else if(gamepad1.dpad_right){
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 2;
                telemetry.addData("Autonomous 2", "= 2 Beacon");
                telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
            }else if (gamepad1.dpad_down) {
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 3;
                telemetry.addData("Autonomous 3","= 1  Beacon and Ramp");
                telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
            }else if(gamepad1.dpad_left) {
                telemetry.clearAll();
                randomnumberthatweneedforsomething = 4;
                telemetry.addData("Autonomous 4", "= 1 Beacon and Hit Cap Ball");
                telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
            }
            telemetry.update();
        }

        //Add telemetry
        telemetry.clearAll();
        telemetry.addData("Auton", " Selected");
        telemetry.update();

        waitForStart();

        //Make sure the gyro is calibrated.
        while (!opModeIsActive() && Drivetrain.gyro.isCalibrating())  {
            sleep(50);
            idle();
        }


        //Show that we are running Autonomous
        telemetry.clearAll();
        telemetry.addData("Running Auton", " ");
        telemetry.update();

        //Reset all the things
        Drivetrain.resetEncoders();
        Drivetrain.resetGyro();

        if (randomnumberthatweneedforsomething == 1){//Shoot and Park on Center Auton

            //Wait for partner to hit beacon
            sleep(20000);

            //Check if OpMode is still running
            if (opModeIsActive()){
                //Start Flywheel
                Flywheel_Intake.setFlywheel(true);
            }

            //Drive Straight For 35 inches
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(35)){
                Drivetrain.setLeftRightPower(-0.6, -0.6);
            }
            Drivetrain.STOP();

            //Wait for robot to settle
            sleep(500);

            //Shoot particles
            if(opModeIsActive()){
                Drivetrain.resetEncoders();
                Flywheel_Intake.setIntakeMode(1);
                sleep(2000);
                Flywheel_Intake.setIntakeMode(0);
                Flywheel_Intake.setFlywheel(false);
            }

            //Park
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(35)){
                Drivetrain.setLeftRightPower(-0.6, -0.6);
            }
            Drivetrain.STOP();

        }else {///////////////////////Base One Beacon///////////////////////

            //Drive Straight For 15 inches
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(15)){
                Drivetrain.setLeftRightPower(-0.3, -0.3);
                updateData();
            }

            //Turn on Flywheel
            if(opModeIsActive()){
                Flywheel_Intake.AutoShoot(true, false);
            }

            //Turn to the goal
            while (opModeIsActive() && Drivetrain.getGyro() > -3){
                Drivetrain.setLeftRightPower(0.2, -0.2);
                updateData();
            }
            while (opModeIsActive() && Drivetrain.getGyro() > -14){
                Drivetrain.setLeftRightPower(0.1, -0.1);
                updateData();
            }
            Drivetrain.STOP();

            //Shoot particles
            if(opModeIsActive()){
                sleep(400);
                Flywheel_Intake.AutoShoot(true, true);
                sleep(300);
                Flywheel_Intake.AutoShoot(true, false);
                sleep(700);
                Flywheel_Intake.AutoShoot(true, true);
                sleep(1000);
                Flywheel_Intake.AutoShoot(false, false);
                Flywheel_Intake.setIntakeMode(0);
            }

            //Turn to beacon
            while (opModeIsActive() && Drivetrain.getGyro() < 35){
                Drivetrain.setLeftRightPower(-0.5, 0.5);
                sleep(10);
                Drivetrain.STOP();
                updateData();
            }
            Drivetrain.STOP();

            //Drive forward until White line is detected
            while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                Drivetrain.DriveStraighttoWhiteLine(0.3, true);
                if(Drivetrain.getUltrasonic() < 30){ //Emergency Stop
                    if (emergency = true){
                        emergencystop.reset();
                        emergency = false;
                    }

                    if(emergencystop.seconds() > 3){
                        super.stop();
                    }
                }else {
                    emergencystop.reset();
                }
                updateData();
            }
            Drivetrain.STOP();

            sleep(100);

            //Drive backward until white line
            while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                Drivetrain.setLeftRightPower(0.2, 0.2);
                sleep(40);
                Drivetrain.STOP();
            }

            //Turn to face beacon directly
            while (opModeIsActive() && Drivetrain.getGyro() < 80){
                Drivetrain.setLeftRightPower(-0.2, 0.2);
            }
            Drivetrain.STOP();

            sleep(100);

            while (opModeIsActive() && Drivetrain.getGyro() > 90){
                Drivetrain.setLeftRightPower(0.2, -0.2);
                sleep(10);
                Drivetrain.STOP();
            }
            Drivetrain.STOP();

            //Back away from beacon if to close
            while (opModeIsActive() && Drivetrain.getUltrasonic() < 20){
                Drivetrain.setLeftRightPower(0.2, 0.2);
            }

            Drivetrain.STOP();

            //Lower Beacon pressers
            Beacon.BumperSynchronised(false);

            //Drive until really close to beacon
            while (opModeIsActive() && Drivetrain.getUltrasonic() > 18){
                Drivetrain.setLeftRightPower(-0.1, -0.1);
            }
            Drivetrain.STOP();

            //Detect the color and raise the appropriate presser
            Beacon.HitButton(false);

            sleep(500);

            //Press button
            Drivetrain.setLeftRightPower(-0.2, -0.2);
            sleep(500);
            Drivetrain.resetEncoders();

            //Back away from beacon
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(5)){
                Drivetrain.setLeftRightPower(0.2, 0.2);
            }
            Drivetrain.STOP();

            //Lift Beacon pressers
            Beacon.BumperSynchronised(true);


            ///////////////////////////////////////////////////////////////////////////////////
            //  Auton Picker                                                                 //
            //  Note: See CC9889_AltAutoBlue for beautifully commented code for this section //
            ///////////////////////////////////////////////////////////////////////////////////
            if (randomnumberthatweneedforsomething == 2){
                //2 BEACON AUTONOMOUS

                //Turn to the Beacon
                while (opModeIsActive() && Drivetrain.getGyro() > 4){
                    Drivetrain.setLeftRightPower(0.3, -0.3);
                    sleep(40);
                    Drivetrain.STOP();
                }

                Drivetrain.setLeftRightPower(-0.5, -0.5);
                sleep(1000);

                while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                    Drivetrain.DriveStraighttoWhiteLine(0.3, true);
                    if(Drivetrain.getUltrasonic() < 10){
                        if (emergency = true){
                            emergencystop.reset();
                            emergency = false;
                        }

                        if(emergencystop.seconds() > 3){
                            super.stop();
                        }
                    }else {
                        emergencystop.reset();
                    }
                    updateData();
                }

                Drivetrain.STOP();

                sleep(100);

                while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                    Drivetrain.setLeftRightPower(0.2, 0.2);
                    sleep(40);
                    Drivetrain.STOP();
                }

                while (opModeIsActive() && Drivetrain.getGyro() < 75){
                    Drivetrain.setLeftRightPower(-0.2, 0.2);
                }
                Drivetrain.STOP();

                while (opModeIsActive() && Drivetrain.getGyro() < 90){
                    Drivetrain.setLeftRightPower(0.1, -0.1);
                    sleep(10);
                    Drivetrain.STOP();
                }
                Drivetrain.STOP();

                Beacon.BumperSynchronised(false);

                //Drive to the beacon
                while (opModeIsActive() && Drivetrain.getUltrasonic() > 30){
                    Drivetrain.setLeftRightPower(-0.1, -0.1);
                }

                Drivetrain.STOP();

                if(Drivetrain.getGyro() > 90 && opModeIsActive()){
                    while (opModeIsActive() && Drivetrain.getGyro() > 90){
                        Drivetrain.setLeftRightPower(0.2, -0.2);
                        sleep(20);
                        Drivetrain.STOP();
                        updateData();
                    }
                }else if(Drivetrain.getGyro() < 90 && opModeIsActive()){
                    while (opModeIsActive() && Drivetrain.getGyro() < 90){
                        Drivetrain.setLeftRightPower(-0.2, 0.2);
                        sleep(20);
                        Drivetrain.STOP();
                        updateData();
                    }
                }

                while (opModeIsActive() && Drivetrain.getUltrasonic() > 18){
                    Drivetrain.setLeftRightPower(-0.1, -0.1);
                }

                Drivetrain.STOP();

                telemetry.addData("Color", Beacon.getColor());
                telemetry.update();

                //Detect the color and raise the appropriate presser
                Beacon.HitButton(false);

                sleep(500);

                Drivetrain.setLeftRightPower(-0.2, -0.2);

                sleep(500);

                Drivetrain.resetEncoders();

                sleep(500);

                Beacon.BumperSynchronised(true);

                Drivetrain.setLeftRightPower(1.0, 0.4);
                sleep(2000);
                Drivetrain.setLeftRightPower(0.0, 0.0);

                super.stop();


            }else if (randomnumberthatweneedforsomething == 3){//Park on Ramp

                while (opModeIsActive() && Drivetrain.getGyro() > 10){
                    Drivetrain.setLeftRightPower(0.2, 0.0);
                    updateData();
                }
                Drivetrain.STOP();

                Drivetrain.setLeftRightPower(0.3, 0.3);
                sleep(1000);

                Drivetrain.STOP();

            }else if (randomnumberthatweneedforsomething == 4){//Cap Ball Park

                Drivetrain.STOP();
                Drivetrain.resetEncoders();

                //Drive Backward
                while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(30)){
                    Drivetrain.setLeftRightPower(0.3, 0.3);
                }
                Drivetrain.STOP();

                while (opModeIsActive() && Drivetrain.getGyro() > -50){
                    Drivetrain.setLeftRightPower(0.4, -0.3);
                    updateData();
                }

                Drivetrain.STOP();

                Drivetrain.resetEncoders();

                while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(10)){
                    Drivetrain.setLeftRightPower(-0.2, -0.2);
                }
                Drivetrain.STOP();

            }else {
                telemetry.addData("Invald", " Autonomous Mode");
                telemetry.update();
                sleep(15000);
            }
            super.stop();
        }
    }

    private void updateData(){
        telemetry.addData("Right Speed", Drivetrain.getRightPower());
        telemetry.addData("Left Speed", Drivetrain.getLeftPower());
        telemetry.addData("Right Encoder", Drivetrain.getRightEncoder());
        telemetry.addData("Left Encoder", Drivetrain.getLeftEncoder());
        telemetry.addData("Right Encoder in Inches", Drivetrain.getRightEncoderinInches());
        telemetry.addData("Left Encoder in Inches", Drivetrain.getLeftEncoderinInches());
        telemetry.addData("Gyro Z-axis", Drivetrain.getGyro());
        telemetry.addData("Ultrasonic Sensor Raw Value", Drivetrain.getUltrasonic());
        telemetry.addData("Back ODS", Drivetrain.getBackODS());
        telemetry.addData("Front ODS", Drivetrain.getFrontODS());
        telemetry.update();
    }

}