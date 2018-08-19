/*
 * Written by Abdul Mohsi Jawaid
 * Testing the Color Sensor
 * Measures red channel from the color sensor
 * Press down key to exit program
 */


import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensorTest 
{
    public static void main(String[] args) 
    {
        EV3ColorSensor colorSensor;
        SampleProvider colorProvider;
        float[] colorSample;
        
        // Color Sensor assumed to be attached on port 1
        Port sensorPort = LocalEV3.get().getPort("S1");
        colorSensor = new EV3ColorSensor(sensorPort);
        colorProvider = colorSensor.getRGBMode();
        colorSample = new float[colorProvider.sampleSize()];
        
        // Exits test if Down button pressed
        while(Button.DOWN.isUp())
        {
            colorProvider.fetchSample(colorSample, 0);
            System.out.println("R" + colorSample[0]);
        }
        colorSensor.close();   
    }
}
