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
import lejos.remote.ev3.RMIRegulatedMotor;

import java.lang.Runnable;
import java.lang.Thread;

public class UltrasonicSensor
{
    RemoteEV3 ev3;
    RMISampleProvider sp;
    float sample;
    RMIRegulatedMotor motor;
    boolean running = true;

    boolean increaseAngle = true;
    private int currentAngle = 0;

    public void connect(RemoteEV3 ev3) throws RemoteException
    {
        this.ev3 = ev3;
        sp = RobotUtility.getUltrasonicSensor(ev3);
        float sample = sp.fetchSample()[0];
        motor = RobotUtility.getPanningMotor(ev3);
        motor.setSpeed(motor.getSpeed()/2);
        running = true;

        new Thread(new Runnable(){
            public void run(){
                //updateMotor();
            }
        }).start();
    }

    public int getAngle(){
        try{
            return motor.getTachoCount();
        } catch (RemoteException e){
            System.out.println("Failed to get motor position");
            return -1;
        }
    }

    public void updateMotor(){
        while(running)
        {
            try{
                if(increaseAngle){
                    motor.rotateTo(45);
                    increaseAngle = false;
                } else{
                    motor.rotateTo(-45);
                    increaseAngle = true;
                }
            } catch(RemoteException e){
                System.out.println("Failed to update motor");
            }
        }
    }

    public float getDistance()
    {
        try{
            return sp.fetchSample()[0];
            
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