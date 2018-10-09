/*
* Provides an interface to the UltraSonic sensor
*/

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
import lejos.hardware.motor.EV3MediumRegulatedMotor;

import lejos.remote.ev3.RMISampleProvider;

public class UltrasonicSensor
{
    RemoteEV3 ev3;
    RMISampleProvider sp;
    float sample;
    EV3MediumRegulatedMotor motor;
    boolean running = true;

    boolean increaseAngle = true;
    private int currentAngle = 0;

    public void connect(RemoteEV3 ev3) throws RemoteException
    {
        this.ev3 = ev3;
        sp = RobotUtility.getUltrasonicSensor(ev3);
        float sample = sp.fetchSample()[0];
        motor = RobotUtility.getPanningMotor(ev3);
        running = true;
    }

    public int getAngle(){
        return currentAngle;
    }

    public void updateMotor()
    {
        if(running)
        {
            if(increaseAngle){
                currentAngle += 5;
                motor.rotate(5);
                if(currentAngle > 45){
                    increaseAngle = false;
                }
            } else{
                currentAngle -= 5;
                motor.rotate(-5);
                if(currentAngle < -45){
                    increaseAngle = true;
                }
            }
        }
    }

    public int getDistance()
    {
        try{
            return (int) sp.fetchSample()[0];
            
        }
        catch(RemoteException e){
            System.out.println("Error attempting to fetch ultrasonic sample");

            return 0;
        }
    }

    public void disconnect()
    {
        running = false;
    }
}