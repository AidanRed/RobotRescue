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

public class Controller
{
    // Delay between each run of the update loop in milliseconds
    private static final updateDelay = 10;
    // Delay between each run of the update motor loop in milliseconds
    private static final motorDelay = 10;

    RemoteEV3 ev3;
    Motor motor;
    ColorSensor colorSensor;
    GyroSensor gyroSensor;
    UltrasonicSensor ultraSensor;
    Gui gui;
    boolean connected = false;

    String lastDisplayText = "";

    int angle = 0;
    
    List<int> distances;

    String color = "NONE";

    public void init(Gui gui)
    {
        this.gui = gui;
        motor = new Motor();
        colorsensor = new ColorSensor();
        gyrosensor = new GyroSensor();
        ultrasonicsensor = new UltrasonicSensor();

        distances = new ArrayList<>();
    }

    public void connect() throws RemoteException, MalformedURLException, NotBoundException
    {
        ev3 = RobotUtility.findBrick(RobotUtility.BRICK_NAME);

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

        // Set up motor loop
        ActionListener motorRunner = new ActionListener() {
            public void actionPerformed(ActionEvent e){
                ultraSensor.updateMotor();
                distances.add(ultraSensor.getDistance()); // TODO: possibly change this so that the motor just constantly moves and the readings just look at the angle as they happen
            }
        };

        new Timer(updateDelay, updateRunner).start();
        new Timer(motorDelay, motorRunner).start();
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
        color = colorSensor.detectColor();

        displaySensorInformation();
    }

    public void displaySensorInformation()
    {
        if(connected == true)
        {
            gui.setText("COLOR: " + color + " Angle: " + Integer.toString(angle) + " Distance: " + Integer.toString(distance[0])); // TODO: update distance displaying to pop entries and display on map
        }
    }

    public void disconnect()
    {
        motor.disconnect();
        colorsensor.disconnect();
        gyrosensor.disconnect();
        ultrasonicsensor.disconnect();

        RobotUtility.closeAllPorts();
    }
}