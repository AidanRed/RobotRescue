import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.remote.ev3.RemoteEV3;

import lejos.utility.Delay;
import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.hardware.port.SensorPort;
import lejos.hardware.motor.RegulatedMotor;

import lejos.remote.ev3.RMISampleProvider;

public class UltrasonicSensor implements Runnable
{
    RemoteEV3 ev3;
    RMISampleProvider sp;
    float [] sample;
    RegulatedMotor motor;

    public void connect(RemoteEV3 ev3) throws RemoteException
    {

        this.ev3 = ev3;
        sp = ev3.createSampleProvider("S3", "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");
        float[] sample = sp.fetchSample();
        motor = new RegulatedMotor(ev3.getPort("C"));
    }

    public void run()
    {
        int currAngle = 0;
        int dist = 0;
        String toPrint = new String();
        while(running)
        {
            try
            {
                while(currAngle<=45)
                {
                    dist = getDistance();
                    toPrint = "Angle is: "+currAngle+" distance is: "+dist;
                    System.out.println(toPrint);
                    currAngle += 5;
                    motor.rotate(5);        
                }
                currAngle -= 5;
                motor.rotate(-5);
                while(currAngle>=-45)
                {
                    int dist = getDistance();
                    String toPrint = "Angle is: "+currAngle+" distance is: "+dist;
                    System.out.println(toPrint);
                    currAngle -= 5;
                    motor.rotate(-5);
                }
            }
            catch(InterruptedException e)
            {
                running = false; 
            }
            
        }
        
    }

    public int getDistance()
    {
        try{
            sample = sp.fetchSample();
            System.out.println(sample[0]);
            
        }
        catch(RemoteException e){
            
        }
        return (int) sample[0];
        
    }

    public 

    public void disconnect()
    {
        //gyroSensor.close();
        try{
            sp.close();
            motor.close();
        }
        catch(RemoteException e){

        }
    }
}