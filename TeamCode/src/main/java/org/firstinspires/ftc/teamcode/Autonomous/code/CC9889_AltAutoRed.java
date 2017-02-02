package org.firstinspires.ftc.teamcode.Autonomous.code;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.Subsystems.*;

/**
 * Created by Joshua H on 1/21/2017.
 */

@Autonomous(name="Red", group="Red")

public class CC9889_AltAutoRed extends LinearOpMode{

    Flywheel Flywheel_Intake                 = new Flywheel();
    Drivebase Drivetrain              = new Drivebase();
    Beacon Beacon                     = new Beacon();
    waitForTick waitForTick           = new waitForTick();

    @Override
    public void runOpMode(){

        int randomnumberthatweneedforsomething = 1;

        //Init the robot
        telemetry.addData("Beacon", "");
        telemetry.update();
        Beacon.init(hardwareMap);
        telemetry.addData("Flywheel", "");
        telemetry.update();
        Flywheel_Intake.init(hardwareMap);
        telemetry.addData("Drivetrain", "");
        telemetry.update();
        Drivetrain.init(hardwareMap);

        while (!gamepad1.a) {
                telemetry.clearAll();
                if (gamepad1.dpad_up) {
                    randomnumberthatweneedforsomething = 1;
                    telemetry.addData("Autonomous 1", "= Shoot and Park on Center");
                } else if (gamepad1.dpad_right) {
                    randomnumberthatweneedforsomething = 2;
                    telemetry.addData("Autonomous 2", "= 2 Beacon");
                } else if (gamepad1.dpad_down) {
                    randomnumberthatweneedforsomething = 3;
                    telemetry.addData("Autonomous 3", "= 1  Beacon and Ramp");
                } else if (gamepad1.dpad_left) {
                    randomnumberthatweneedforsomething = 4;
                    telemetry.addData("Autonomous 4", "= 1 Beacon and Hit Cap Ball");
                } else {
                    telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
                }

                telemetry.update();

            //Add telemetry

            telemetry.addData("Please Select an Autonomous Mode", " then press the A button");
            //telemetry.addData(">", "Gyro Calibrated. ¯\\_(ツ)_/¯");
            telemetry.update();
        }
        telemetry.clearAll();
        telemetry.addData("Auton", " Selected");
        telemetry.update();


        waitForStart();

        telemetry.clearAll();
        telemetry.addData("Running Auton", " ");
        telemetry.update();

        //Reset all the things
        Drivetrain.resetEncoders();
        Drivetrain.resetGyro();
        sleep(100);


        waitForStart();
        Drivetrain.resetEncoders();
        Drivetrain.resetGyro();

        telemetry.addData("Runnig Auton", " ");

        if (randomnumberthatweneedforsomething == 1){//Shoot and Park on Center Auton
            //Wait for partner to hit beacon
            sleep(20000);

            if (opModeIsActive()){
                //Start Flywheel
                Flywheel_Intake.setFlywheel(true);
            }

            //Drive Straight For 35 inches
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(35)){
                Drivetrain.setLeftRightPower(-0.1, -0.1);
                updateData();
            }

            Drivetrain.STOP();

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

            //Drive Straight For 22 inches
            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(22)){
                Drivetrain.setLeftRightPower(-0.1, -0.1);
                updateData();
            }

            Drivetrain.STOP();

            Flywheel_Intake.setFlywheel(true);

            //Turn to the goal
            while(opModeIsActive() && Drivetrain.TurnAreWeThereYet(-6)){
                Drivetrain.turnAbsolute(-6, 0.2);
                updateData();
            }

            while(opModeIsActive() && Drivetrain.TurnAreWeThereYet(-15)){
                Drivetrain.turnAbsolute(-15, 0.1);
            }

            Drivetrain.STOP();

            /*while (opModeIsActive() && robot.getGyro() > -6){
                robot.Drivetrain(0.2, -0.2);
                updateData();
                robot.waitForTick(25);
            }
            while (opModeIsActive() && robot.getGyro() > -15){
                robot.Drivetrain(0.1, -0.1);
                updateData();
                robot.waitForTick(25);
            }*/

            //Shoot particles
            if(opModeIsActive()){
                sleep(500);
                Flywheel_Intake.setIntakeMode(1);
                sleep(2000);
                Flywheel_Intake.setIntakeMode(0);
                Flywheel_Intake.setFlywheel(false);
            }

            while (opModeIsActive() && Drivetrain.TurnAreWeThereYet(30)){
                Drivetrain.turnAbsolute(30, 0.2);
            }

            Drivetrain.STOP();

            /*while (opModeIsActive() && robot.getGyro() < 5)
                robot.Drivetrain(-0.2, 0.2);
                updateData();
                robot.waitForTick(25);
            }
            while (opModeIsActive() && robot.getGyro() < 30){
                robot.Drivetrain(-0.1, 0.1);
                updateData();
                robot.waitForTick(25);
            }*/

            //Go Straight till line
            while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                Drivetrain.DriveStraighttoWhiteLine(0.7, true);
            }

            while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                Drivetrain.DriveStraighttoWhiteLine(0.1, false);
            }

            Drivetrain.STOP();


            while (opModeIsActive() && !Drivetrain.getFrontODS_Detect_White_Line()){
                Drivetrain.CenterOnWhiteLine(0.1, true);
            }

            while (opModeIsActive() && Drivetrain.getFrontODS_Detect_White_Line()){
                Drivetrain.setLeftRightPower(0.1,-0.1);
                waitForTick.function(50);
            }

            Drivetrain.STOP();

            /*
            while (opModeIsActive() && robot.getFrontODS() < 1.0){
                robot.Drivetrain(-0.1, 0.1);
                updateData();
                robot.waitForTick(50);
            }
            while (opModeIsActive() && robot.getFrontODS() > 1.0){
                robot.Drivetrain(0.1, -0.1);
                updateData();
                robot.waitForTick(50);
            }
            */

            Beacon.BumperSynchronised(false);

            //Drive to the beacon
            Drivetrain.setLeftRightPower(-0.1, -0.1);
            sleep(600);

            Beacon.HitButton(false);

            sleep(700);

            Drivetrain.resetEncoders();

            while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(5)){
                Drivetrain.DriveBackwardstoDistance(0.4, 5);
                waitForTick.function(50);
            }

            Drivetrain.STOP();

            Beacon.BumperSynchronised(true);

            ////////////////////////////////////////////////////////
            /////       Auton Picker                        ////////
            ////////////////////////////////////////////////////////

            if (randomnumberthatweneedforsomething == 2){
                //2 BEACON AUTONOMOUS
                //Turn to the Beacon

                while (opModeIsActive() && Drivetrain.TurnAreWeThereYet(70)){
                    Drivetrain.turnAbsolute(70, 0.2);
                }

                while (opModeIsActive() && Drivetrain.TurnAreWeThereYet(6)){
                    Drivetrain.turnAbsolute(6, 0.1);
                }

                Drivetrain.STOP();

                Drivetrain.setLeftRightPower(-0.7, -0.7);
                sleep(1000);

                while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                    Drivetrain.DriveStraighttoWhiteLine(0.4, true);
                }
                while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                    Drivetrain.DriveStraighttoWhiteLine(0.1, false);
                }

                Drivetrain.STOP();

                while (opModeIsActive() && !Drivetrain.getFrontODS_Detect_White_Line()){
                    Drivetrain.CenterOnWhiteLine(0.2, true);
                }
                while (opModeIsActive() && Drivetrain.getFrontODS_Detect_White_Line()){
                    Drivetrain.CenterOnWhiteLine(0.2, false);
                }

                Drivetrain.STOP();

                Beacon.BumperSynchronised(false);

                while (opModeIsActive() && Drivetrain.getUltrasonic() > 20){
                    Drivetrain.DriveToUltrasonicDistance(0.1, 20);
                }

                Beacon.HitButton(true);

                Drivetrain.setLeftRightPower(-0.1, -0.1);
                sleep(500);

                Drivetrain.resetEncoders();


                while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(8)){
                    Drivetrain.DriveBackwardstoDistance(0.1, 8);
                }

                Drivetrain.STOP();

                super.stop();

            }else if (randomnumberthatweneedforsomething == 3){//Park on Ramp

                while (opModeIsActive() && Drivetrain.TurnAreWeThereYet(6)){
                    Drivetrain.turnAbsolute(6, 0.1);
                }

                Drivetrain.STOP();

                Drivetrain.setLeftRightPower(-0.1, -0.1);
                sleep(1000);

                while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                    Drivetrain.DriveStraighttoWhiteLine(0.6, true);
                }

                while (opModeIsActive() && !Drivetrain.getBackODS_Detect_White_Line()){
                    Drivetrain.DriveStraighttoWhiteLine(0.1, false);
                }

                Drivetrain.STOP();

                while (opModeIsActive() && !Drivetrain.getFrontODS_Detect_White_Line()){
                    Drivetrain.CenterOnWhiteLine(0.1, true);
                }

                while (opModeIsActive() && Drivetrain.getFrontODS_Detect_White_Line()){
                    Drivetrain.CenterOnWhiteLine(0.1, false);
                }

                Drivetrain.STOP();

                Beacon.BumperSynchronised(true);

                Drivetrain.setLeftRightPower(-0.1, -0.1);
                sleep(600);

                Beacon.HitButton(true);

                Drivetrain.STOP();
                Drivetrain.resetEncoders();

                while(opModeIsActive() && Drivetrain.InchesAreWeThereYet(3)){
                    Drivetrain.DriveBackwardstoDistance(0.2, 3);
                }

                while(opModeIsActive() && Drivetrain.TurnAreWeThereYet(10)){
                    Drivetrain.turnAbsolute(10, 0.2);
                }

                Drivetrain.STOP();

                Drivetrain.setLeftRightPower(0.7, 0.7);
                sleep(1000);

                Drivetrain.STOP();

            }else if (randomnumberthatweneedforsomething == 4){//Cap Ball Park

                Drivetrain.STOP();
                Drivetrain.resetEncoders();

                //Drive Backward
                while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(30)){
                    Drivetrain.DriveBackwardstoDistance(0.3, 30);
                }
                Drivetrain.STOP();

                Drivetrain.setLeftRightPower(0.4, -0.4);
                sleep(1000);
                Drivetrain.STOP();
                Drivetrain.resetEncoders();
                sleep(1000);

                while (opModeIsActive() && Drivetrain.InchesAreWeThereYet(10)){
                    Drivetrain.setLeftRightPower(-0.4, -0.4);
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

    public void updateData(){
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