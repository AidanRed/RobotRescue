/*
* Provides an interface to the ColorSensor
*/
import java.rmi.RemoteException;

import lejos.hardware.Button;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;
import lejos.robotics.Color;
import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RMISampleProvider;
import lejos.utility.Delay;

public class ColorSensor
{
    RemoteEV3 ev3;
    RMISampleProvider sp;
    int sample;

    boolean running = false;

    public void connect(RemoteEV3 ev3)
    {
        this.ev3 = ev3;
        sp = RobotUtility.getColorSensor(ev3);
        sample = detectColor();
        running = true;
    }

    public String detectColor()
    {
        try{
            int currentDetectColor = (int) sp.fetchSample[0];
        } catch(RemoteException e){
            System.out.println("Error attempting to fetch colour sample");

            return "NONE";
        }
        String color = "";
        switch(currentDetectColor){
            case Color.RED:
                color = "RED";
                break;
            case Color.BLUE:
                color = "BLUE";
                break;
            case Color.GREEN:
                color = "GREEN";
                break;
            case Color.BLACK:
                color = "BORDER";
                break;
            default:
                color = "NONE";
                break;
        }

        return color;
    }

    public void disconnect()
    {
        running = false;
    }

}