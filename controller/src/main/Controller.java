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
    boolean connected = false;

    String lastDisplayText = "";

    int angle = 0;
    
    float distance = 0;

    String color = "NONE";

    public void init(Gui gui)
    {
        this.gui = gui;
        motor = new Motor();
        colorSensor = new ColorSensor();
        gyroSensor = new GyroSensor();
        ultraSensor = new UltrasonicSensor();

        //distances = new ArrayList<>();
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
                System.out.println("Moved Forward");
                motor.moveForward();
                break;
            case "move_backward":
                System.out.println("Moved Backward");
                motor.moveBackward();
                break;
            case "turn_left":
                //angle = (angle - 1) % 360;
                System.out.println("Turned Left");
                motor.turnLeft();
                gui.setMapAngle((int)angle);
                break;
            case "turn_right":
                //angle = (angle + 1) % 360;
                System.out.println("Turned Right");
                motor.turnRight();
                gui.setMapAngle((int)angle);
                break;
            case "stop":
                motor.stop();
                break;
            default:
                System.out.println("Invalid action");
        }
    }

    private void update(){
        angle = gyroSensor.getAngle();
        gui.setMapAngle(angle);
        color = colorSensor.detectColor();
        distance = ultraSensor.getDistance() * 100f;
        if(distance != Float.POSITIVE_INFINITY){
            double theta = Math.toRadians((ultraSensor.getAngle() + angle) - 90);
            int dx = (int)(Math.cos(theta) * distance);
            int dy = (int)(Math.sin(theta) * distance);
            gui.addPoint(gui.getMapWidth() / 2 + dx, gui.getMapHeight() / 2 + dy);
        }
        if(motor.timeStarted>motor.timeStopped)
        {
            // motor running
            double timepassed = Math.min(motor.timeStopped-motor.timeStarted, updateDelay);
            double robotDistance = 180.0 * timepassed;
            double theta = Math.toRadians(angle);
            int robotX = (int)(Math.cos(theta) * robotDistance);
            int robotY = (int)(Math.sin(theta) * robotDistance);
            gui.incRobotPos(robotX,robotY);
        }
        displaySensorInformation();
    }

    public void displaySensorInformation()
    {
        if(connected == true)
        {
            gui.setText("COLOR: " + color + " Angle: " + Integer.toString(angle) + " Distance: " + Float.toString(distance)); // TODO: update distance displaying to pop entries and display on map
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
        }
    }
}