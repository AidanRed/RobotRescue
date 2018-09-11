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
    Gui gui;
    boolean connected = false;

    public void init(Gui gui)
    {
        this.gui = gui;
        motor = new Motor();
        colorsensor = new ColorSensor();

    }

    public void connect() throws RemoteException, MalformedURLException, NotBoundException
    {
        ev3 = new RemoteEV3("192.168.43.132");
        motor.connect(ev3);
        colorsensor.connect(ev3);
        connected = true;
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
                System.out.println("Turned Left");
                motor.turnLeft();
                break;
            case "turn_right":
                System.out.println("Turned Right");
                motor.turnRight();
                break;
            case "stop":
                System.out.println("Stopped");
                motor.stop();
                break;
            default:
                System.out.println("Invalid action");
        }
    }

    public void printColorInformation()
    {
        System.out.println("Color:");
        if(connected == true)
        {
            colorsensor.detectColor();
        }
    }

    public void disconnect()
    {
        motor.disconnect();
        colorsensor.disconnect(); 
    }
}