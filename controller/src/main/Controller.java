/*
* Author: Abdul Mohsi Jawaid
* Communicates between the GUI and the hardware 
*/


import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.hardware.Button;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.Color;
import lejos.remote.ev3.RemoteEV3;
import lejos.utility.Delay;

public class Controller
{

    RemoteEV3 ev3;
    Motor motor;
    ColorSensor colorsensor;
    //GyroSensor gyrosensor;
    Gui gui;
    boolean connected = false;

    int angle = 0;

    public void init(Gui gui)
    {
        this.gui = gui;
        motor = new Motor();
        colorsensor = new ColorSensor();
        //gyrosensor = new GyroSensor();

    }

    public void connect() throws RemoteException, MalformedURLException, NotBoundException
    {
        ev3 = new RemoteEV3("192.168.43.132");
        motor.connect(ev3);
        colorsensor.connect(ev3);
        //gyrosensor.connect(ev3);
        gui.log("Connected");
        connected = true;
    }

    public void action(String a)
    {
        switch(a){
            case "move_forward":
                System.out.println("Moved Forward");
                motor.moveForward();
                printSensorInformation();
                break;
            case "move_backward":
                System.out.println("Moved Backward");
                motor.moveBackward();
                printSensorInformation();
                break;
            case "turn_left":
                angle = (angle - 1) % 360;
                System.out.println("Turned Left");
                motor.turnLeft();
                printSensorInformation();
                gui.setMapAngle((int)(angle/50.0*360));
                break;
            case "turn_right":
                angle = (angle + 1) % 360;
                System.out.println("Turned Right");
                motor.turnRight();
                printSensorInformation();
                gui.setMapAngle((int)(angle/50.0*360));
                break;
            case "stop":
                //System.out.println("Stopped");
                motor.stop();
                break;
            default:
                System.out.println("Invalid action");
        }
    }

    public void printSensorInformation()
    {
        if(connected == true)
        {
            gui.setText("COLOR: "+colorsensor.detectColor()+" Angle: "+angle);
        }
    }

    public void disconnect()
    {
        motor.disconnect();
        colorsensor.disconnect();
        //gyrosensor.disconnect(); 
    }
}