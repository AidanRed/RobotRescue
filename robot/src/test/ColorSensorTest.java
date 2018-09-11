/*
 * Written by Abdul Mohsi Jawaid
 * Testing the Color Sensor
 * Measures red channel from the color sensor
 * Press down key to exit program
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

public class ColorSensorTest 
{
    public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException 
    {
        RemoteEV3 ev3 = new RemoteEV3("192.168.43.132");
        EV3ColorSensor colorSensor;
        
        // Color Sensor assumed to be attached on port 1

        Port sensorPort = ev3.getPort("S1");
        colorSensor = new EV3ColorSensor(sensorPort);
        
        // Exits test if Down button pressed
        while(Button.DOWN.isUp())
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
                default:
                    System.out.println("NONE DETECTED");
                    break;

            }
            Delay.msDelay(100);
        }
        colorSensor.close();   
    }
}