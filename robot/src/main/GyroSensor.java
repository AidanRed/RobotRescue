/*
* Provides an interface to the GyroscopeSensor
*/

import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RMISampleProvider;

import java.rmi.RemoteException;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;


public class GyroSensor
{
    RemoteEV3 ev3;
    RMISampleProvider  sp;
    int sample;

    boolean running = false;

    public void connect(RemoteEV3 ev3) throws RemoteException
    {
        this.ev3 = ev3;
        sp = RobotUtility.getGyroSensor(ev3);
        sample = getAngle();
        running = true;
    }

    public int getAngle()
    {
        try{
            int angle = (int) sp.fetchSample()[0];
            if(angle < 0){
                return 360 + angle;
            }
            return angle;
        }
        catch(RemoteException e){
            System.out.println("Error attempting to fetch gyroscope sample");
        }

        return 0;
    }

    public void disconnect()
    {
        running = false;
    }


}