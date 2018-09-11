/*
* Author: Abdul Mohsi Jawaid
* Inspired by Roland Croft's MotorTest
* Provides an interface to the hardware motors
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

public class ColorSensor
{
    RemoteEV3 ev3;
    EV3ColorSensor colorSensor;

    public void connect(RemoteEV3 ev3)
    {
        this.ev3 = ev3;
        colorSensor = new EV3ColorSensor(ev3.getPort("S1"));
    }

    public void detectColor()
    {
        int currentDetectColor = colorSensor.getColorID();
        switch(currentDetectColor){
            case Color.RED:
                System.out.println("RED DETECTED");
                break;
            case Color.BLUE:
                System.out.println("BLUE DETECTED");
                break;
            case Color.GREEN:
                System.out.println("GREEN DETECTED");
                break;
            case Color.BLACK:
                System.out.println("BORDER DETECTED");
                break;
            default:
                System.out.println("NONE DETECTED");
                break;

        }
    }

    public void disconnect()
    {
        colorSensor.close();
    }

}