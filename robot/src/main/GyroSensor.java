import lejos.remote.ev3.RemoteEV3;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.Gyroscope;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;


public class GyroSensor implements Gyroscope
{
    RemoteEV3 ev3;
    EV3GyroSensor gyroSensor;
    SampleProvider  sp;
    float [] sample;
    int offset = 0;

    public void connect(RemoteEV3 ev3)
    {
        this.ev3 = ev3;
        gyroSensor = new EV3GyroSensor(ev3.getPort("S2"));
        gyroSensor.getAngleAndRateMode();
        sample = new float[gyroSensor.sampleSize()];
        gyroSensor.reset();
    }

    public int getAngle()
    {
        gyroSensor.fetchSample(sample,0);
        System.out.println(sample[0]);
        return (int) sample[0] - offset;
    }

    public float getAngularVelocity()
    {
        gyroSensor.fetchSample(sample, 0);
        return sample[1];
    }

    public void reset()
    {
        gyroSensor.fetchSample(sample,0);
        offset = (int) sample[0];
    }


    // recalibrates gyro
    public void resetGyro()
    {
        gyroSensor.reset();
        offset = 0;
    }

    public void recalibrateOffset()
    {
        resetGyro();
    }

    public void disconnect()
    {
        gyroSensor.close();
    }


}