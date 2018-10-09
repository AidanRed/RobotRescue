/*
* Provides an interface to the hardware motors
*/

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.utility.Delay;
import lejos.hardware.Button;
import lejos.hardware.motor.UnregulatedMotor;
import lejos.remote.ev3.RemoteEV3;

public class Motor
{
    RemoteEV3 ev3;
    UnregulatedMotor motorA;
    UnregulatedMotor motorB;

    public void connect(RemoteEV3 ev3)
    {
        this.ev3 = ev3;
        motorA = new UnregulatedMotor(ev3.getPort("A"));
        motorB = new UnregulatedMotor(ev3.getPort("B"));
    }

    public void moveForward()
    {
        motorA.forward();
        motorB.forward();
        motorA.setPower(25);
        motorB.setPower(25);
    }

    public void moveBackward()
    {
        motorA.backward();
        motorB.backward();
        motorA.setPower(25);
        motorB.setPower(25);
    }

    public void turnLeft()
    {
        motorA.forward();
        motorB.backward();
        motorA.setPower(25);
        motorB.setPower(25);
    }

    public void turnRight()
    {
        motorA.backward();
        motorB.forward();
        motorA.setPower(25);
        motorB.setPower(25);
    }

    public void stop()
    {
        motorA.stop();
        motorB.stop();
    }

    public void disconnect()
    {
        motorA.close();
        motorB.close();
    }

}