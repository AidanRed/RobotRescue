/*
Proof of concept test for the gyroscopic sensor.
Displays current orientation of sensor until escape button is pressed.
 */

import java.rmi.RemoteException;

import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RMISampleProvider;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.KeyListener;
import lejos.hardware.Button;
import lejos.hardware.Key;

public class GyroSensorTest {
    static boolean running = false;

    static void exit(){
        running = false;
        RobotUtility.closeSensors();
    }

    public static void main(String[] args) throws RemoteException{
        RemoteEV3 ev3;
        RMISampleProvider sampler;
        float lastSample;

        running = true;

        ev3 = RobotUtility.findBrick(RobotUtility.BRICK_NAME);

        sampler = RobotUtility.getGyroSensor(ev3);
        lastSample = sampler.fetchSample()[0];

        // Add a listener to the escape button to exit the program
        Button.ESCAPE.addKeyListener(new KeyListener() {
            public void keyPressed(Key k){
                exit();
            }
            public void keyReleased(Key k){
            }
        });

        GyroSensorTest tester = new GyroSensorTest();
        System.out.println("Outputs on reading change");
        while (running) {
            float newSample = sampler.fetchSample()[0];
            if(newSample != lastSample){
                System.out.println(newSample);
                lastSample = newSample;
            }
        }
    }
}
