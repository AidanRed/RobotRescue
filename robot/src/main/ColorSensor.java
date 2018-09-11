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

    public String detectColor()
    {
        int currentDetectColor = colorSensor.getColorID();
        String color = "";
        switch(currentDetectColor){
            case Color.RED:
                color = "RED DETECTED";
                break;
            case Color.BLUE:
                color = "BLUE DETECTED";
                break;
            case Color.GREEN:
                color = "GREEN DETECTED";
                break;
            case Color.BLACK:
                color = "BORDER DETECTED";
                break;
            default:
                color = "NONE DETECTED";
                break;
        }
        return color;
    }

    public void disconnect()
    {
        colorSensor.close();
    }

}