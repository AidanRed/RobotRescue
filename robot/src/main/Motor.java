/*
* Provides an interface to the hardware motors
*/

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.utility.Delay;
import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RemoteEV3;

public class Motor
{
    RemoteEV3 ev3;
    RMIRegulatedMotor motorLeft;
    RMIRegulatedMotor motorRight;

    public Long timeStarted;
    public Long timeStopped;

    public void connect(RemoteEV3 ev3)
    {
        this.ev3 = ev3;
        motorLeft = RobotUtility.getLeftMotor(ev3);
        motorRight = RobotUtility.getRightMotor(ev3);
        timeStarted = System.currentTimeMillis();
        timeStopped = System.currentTimeMillis();
    }

    public void moveForward()
    {
        try
        {
            motorLeft.forward();
            motorRight.forward();
            motorLeft.setSpeed(180);
            motorRight.setSpeed(180);
            timeStarted = System.currentTimeMillis();
            //motorLeft.rotate(1,true);
            //motorRight.rotate(1,false);
        }
        catch(RemoteException e)
        {
            System.out.println("Error attempting to move backward");
        }
    }

    public void moveBackward()
    {
        try
        {
            motorLeft.backward();
            motorRight.backward();
            motorLeft.setSpeed(180);
            motorRight.setSpeed(180);
            timeStarted = System.currentTimeMillis();
        }
        catch(RemoteException e)
        {
            System.out.println("Error attempting to move backward");
        }
    }

    public void turnLeft()
    {
        try
        {
            motorRight.forward();
            motorLeft.backward();
            motorLeft.setSpeed(180);
            motorRight.setSpeed(180);
            timeStarted = System.currentTimeMillis();
        }
        catch(RemoteException e)
        {
            System.out.println("Error attempting to turn left");
        }
    }

    public void turnRight()
    {
        try
        {
            motorRight.backward();
            motorLeft.forward();
            motorLeft.setSpeed(180);
            motorRight.setSpeed(180);
            timeStarted = System.currentTimeMillis();
        }
        catch(RemoteException e)
        {
            System.out.println("Error attempting to turn right");
        }
    }

    public void stop()
    {
        try
        {
            motorLeft.stop(true);
            motorRight.stop(true);
            timeStopped = System.currentTimeMillis();
        }
        catch(RemoteException e){
            System.out.println("Error attempting to stop motor");
        }
    }

    public void disconnect()
    {
        try
        {
            motorLeft.close();
            motorRight.close();
        }
        catch(RemoteException e)
        {
            System.out.println("Error attempting to close motor ports");
        }
    }

}