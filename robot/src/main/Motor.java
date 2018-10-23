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

    public static final int ROT_SPEED = 180;
    // Centimetres moved from one rotation of the wheel
    public static final double ROTATION_DIST = 18.22;
    // Centimetres per second movement speed
    //public static final double CENT_PER_SEC = ((double)ROT_SPEED / 360) * ROTATION_DIST; 
    public static final double CENT_PER_SEC = 9.11; 

    public int direction = 1;
    public Long timeStarted;
    public Long timeStopped;

    

    public void connect(RemoteEV3 ev3)
    {
        this.ev3 = ev3;
        motorLeft = RobotUtility.getLeftMotor(ev3);
        motorRight = RobotUtility.getRightMotor(ev3);

        try{
            motorLeft.setSpeed(ROT_SPEED);
            motorRight.setSpeed(ROT_SPEED);
        }
        catch(RemoteException e){
            System.out.println("Error failed to set motor speed");
        }

        timeStarted = 0L;
        timeStopped = 1L;
    }

    public void moveForward()
    {
        try
        {
            direction = 1;
            motorLeft.forward();
            motorRight.forward();
            
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
            direction = -1;
            motorLeft.backward();
            motorRight.backward();
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
            //timeStarted = System.currentTimeMillis();
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
            //timeStarted = System.currentTimeMillis();
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
            if(timeStarted > timeStopped){
                timeStopped = System.currentTimeMillis();
            }
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