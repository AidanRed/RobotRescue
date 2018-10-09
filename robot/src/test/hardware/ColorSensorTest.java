/*
 * Written by Abdul Mohsi Jawaid
 * Testing the Color Sensor
 * Measures red channel from the color sensor
 * Press down key to exit program
 */


import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.remote.ev3.RMISampleProvider;
import lejos.hardware.Button;
import lejos.robotics.Color;
import lejos.remote.ev3.RemoteEV3;
import lejos.hardware.KeyListener;
import lejos.hardware.Button;
import lejos.hardware.Key;

public class ColorSensorTest 
{
    static boolean running = false;

    static void exit(){
        running = false;
        RobotUtility.closeSensors();
    }

    public static void main(String[] args) throws RemoteException
    {
        running = true;
        RemoteEV3 ev3 = RobotUtility.findBrick();
        RMISampleProvider colorSensor = RobotUtility.getColorSensor(ev3);
        int lastColor = (int)colorSensor.fetchSample()[0];
        
        // Add a listener to the escape button to exit the program
        Button.ESCAPE.addKeyListener(new KeyListener() {
            public void keyPressed(Key k){
                exit();
            }
            public void keyReleased(Key k){
            }
        });

        while(running)
        {
            int currentColor = (int)colorSensor.fetchSample()[0];
            if(currentColor != lastColor){
                lastColor = currentColor;
                switch(lastColor){
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
            }
        }
    }
}