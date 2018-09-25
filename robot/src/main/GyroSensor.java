import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RMISampleProvider;

import java.rmi.RemoteException;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.Gyroscope;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


public class GyroSensor implements Gyroscope
{
    RemoteEV3 ev3;
    // EV3GyroSensor gyroSensor;
    RMISampleProvider  sp;
    float [] sample;
    int offset = 0;

    public void connect(RemoteEV3 ev3) throws RemoteException
    {
        this.ev3 = ev3;
        sp = ev3.createSampleProvider("S2", "lejos.hardware.sensor.EV3GyroSensor", "Angle");
        // gyroSensor = new EV3GyroSensor(ev3.getPort("S2"));
        // gyroSensor.getAngleMode();
        float[] sample = sp.fetchSample();
        // gyroSensor.reset();
    }

    public int getAngle()
    {
        try{
            sample = sp.fetchSample();
            System.out.println(sample[0]);
            
        }
        catch(RemoteException e){
            
        }
        return (int) sample[0] - offset;
        
    }

    public float getAngularVelocity()
    {
    //    gyroSensor.fetchSample(sample, 0);
       return sample[1];
    }

    public void reset() 
    {
        
        // sample = sp.fetchSample();
        // offset = (int) sample[0];
    }


    // recalibrates gyro
    public void resetGyro()
    {
        //gyroSensor.reset();
        offset = 0;
    }

    public void recalibrateOffset()
    {
        resetGyro();
    }

    public void disconnect()
    {
        //gyroSensor.close();
        try{
            sp.close();
        }
        catch(RemoteException e){

        }
    }


}