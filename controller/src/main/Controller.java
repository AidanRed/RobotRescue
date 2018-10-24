/*
* Communicates between the GUI and the hardware 
*/


import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

import lejos.hardware.Button;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.Color;
import lejos.remote.ev3.RemoteEV3;
import lejos.utility.Delay;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;

public class Controller
{
    // Delay between each run of the update loop in milliseconds
    private static final int updateDelay = 10;
    // Delay between each run of the update motor loop in milliseconds
    private static final int motorDelay = 10;

    RemoteEV3 ev3;
    Motor motor;
    ColorSensor colorSensor;
    GyroSensor gyroSensor;
    UltrasonicSensor ultraSensor;
    
    Gui gui;
    Navigator nav;

    boolean connected = false;

    String lastDisplayText = "";

    int angle = 0;
    
    float distance = 0;

    String color = "NONE";

    public void init(Gui gui, Navigator nav)
    {
        this.gui = gui;
        this.nav = nav;

        motor = new Motor();
        colorSensor = new ColorSensor();
        gyroSensor = new GyroSensor();
        ultraSensor = new UltrasonicSensor();
    }

    public void connect() throws RemoteException, MalformedURLException, NotBoundException
    {
        ev3 = RobotUtility.findBrick(RobotUtility.BRICK_NAME);
        while(ev3 == null){
            System.out.println("Press enter to retry..");
            try{
                System.in.read();
            }catch(Exception e){

            }
            ev3 = RobotUtility.findBrick(RobotUtility.BRICK_NAME);
        }

        motor.connect(ev3);
        colorSensor.connect(ev3);
        gyroSensor.connect(ev3);
        ultraSensor.connect(ev3);

        gui.log("Connected");
        connected = true;

        // Set up update loop
        ActionListener updateRunner = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                update();
            }
        };
        /*
        // Set up motor loop
        ActionListener motorRunner = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                ultraSensor.updateMotor();
            }
        };
        new Timer(motorDelay, motorRunner).start();*/
        new Timer(updateDelay, updateRunner).start();
    }

    public boolean isConnected(){
        return connected;
    }

    public void action(String a)
    {
        switch(a){
            case "move_forward":
                motor.moveForward();
                break;
            case "move_backward":
                motor.moveBackward();
                break;
            case "turn_left":
                motor.turnLeft();
                break;
            case "turn_right":
                motor.turnRight();
                break;
            case "stop":
                motor.stop();
                break;
            default:
                System.out.println("Invalid action");
        }
    }

    private void update(){
        if(connected){
            angle = gyroSensor.getAngle();
            gui.setMapAngle(angle);
            color = colorSensor.detectColor();
            // Get reading from ultrasonic sensor and convert to centimetres
            distance = ultraSensor.getDistance() * 1000f;
            if(distance != Float.POSITIVE_INFINITY){
                double theta = Math.toRadians(ultraSensor.getAngle() + angle);
                double dx = gui.getRobotX() + -(Math.sin(theta) * distance);
                double dy = gui.getRobotY() + -(Math.cos(theta) * distance);
                gui.addPoint(gui.getMapWidth() / 2 + (int)dx, gui.getMapHeight() / 2 + (int)dy);
            }
            if(motor.timeStarted>motor.timeStopped)
            {
                // motor running
                double startExtra = (System.currentTimeMillis()-motor.timeStarted)/100d;
                double timepassed = Math.min(startExtra, ((double)updateDelay)/100d);
                double robotDistance = (Motor.CENT_PER_SEC * timepassed) * 5.5d * (double)motor.direction;
                double theta = Math.toRadians(angle);
                double robotY = -(Math.cos(theta) * robotDistance);
                double robotX = -(Math.sin(theta) * robotDistance);
                gui.incRobotPos(robotX,robotY);
            }
        }
    }

    public void displaySensorInformation()
    {
        if(connected == true)
        {
            gui.setText("Colour: " + color + " Angle: " + Integer.toString(angle) + " Distance: " + Float.toString(distance)); // TODO: update distance displaying to pop entries and display on map
        }
    }

    public void disconnect()
    {
        if(connected){
            motor.disconnect();
            colorSensor.disconnect();
            gyroSensor.disconnect();
            ultraSensor.disconnect();

            RobotUtility.closeAllPorts();
            connected = false;
            gui.clearLines();
            gui.clearPoints();
            gui.setRobotPos(0, 0);
            gui.setMapAngle(0);
        }
    }
}