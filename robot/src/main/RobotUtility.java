/*
Convenience functions and variables for performing operations on the EV3 Hardware.
*/
import lejos.remote.ev3.RemoteEV3;
import lejos.hardware.BrickFinder;
import lejos.hardware.BrickInfo;
import lejos.hardware.lcd.LCD;
import lejos.hardware.sensor.UARTSensor;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3GyroSensor;

import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

import java.util.ArrayList;
import java.util.List;


/* Convenience class for discovering the EV3 brick and interacting with the sensors */
class RobotUtility{
    public static final String BRICK_NAME = "RescueBot";

    // The sensor ports
    public static final String COLOUR_PORT = "S1";
    public static final String GYRO_PORT = "S2";
    public static final String ULTRASONIC_PORT = "S3";

    // The motor ports
    public static final String RIGHT_PORT = "A";
    public static final String LEFT_PORT = "B";
    public static final String PANNING_PORT = "C";

    // Lists of the sensors and ports currently open
    public static List<UARTSensor> openSensors = new ArrayList<>();
    public static List<RMIRegulatedMotor> openMotors = new ArrayList<>();
    
    // Returns the first brick discovered in the network. If none found, returns null.
    public static RemoteEV3 findBrick() {
        BrickInfo[] bricks = BrickFinder.discover();

        for(BrickInfo info: bricks){
            String the_name = info.getName();
            System.out.println("Discovered brick: " + the_name);
            try{
                RemoteEV3 brick = new RemoteEV3(info.getIPAddress());
                brick.getAudio().systemSound(0);

                return brick;
            }
            catch(RemoteException e){
            }
            catch(MalformedURLException e){
            }
            catch(NotBoundException e){   
            }

            System.out.println("Failed to connect to brick: " + the_name);
            return null;
        }

        System.out.println("No brick found!");
        return null;
    }

    // Returns the brick in the network with the given name if it can be found, otherwise returns null
    public static RemoteEV3 findBrick(String name) {
        BrickInfo[] bricks = BrickFinder.discover();

        for(BrickInfo info: bricks){
            String the_name = info.getName();
            if(the_name.equals(name)){
                try{
                    RemoteEV3 brick = new RemoteEV3(info.getIPAddress());
                    brick.getAudio().systemSound(0);

                    return brick;
                }
                catch(RemoteException e){
                }
                catch(MalformedURLException e){
                }
                catch(NotBoundException e){   
                }

                System.out.println("Failed to connect to brick: " + the_name);
                return null;
            }
        }

        System.out.println("No brick found!");
        return null;
    }

    public static EV3UltrasonicSensor getUltrasonicSensor(RemoteEV3 ev3){
        EV3UltrasonicSensor sensor = new EV3UltrasonicSensor(ev3.getPort(ULTRASONIC_PORT));
        openSensors.add(sensor);

        return sensor;
    }

    public static EV3ColorSensor getColorSensor(RemoteEV3 ev3){
        EV3ColorSensor sensor = new EV3ColorSensor(ev3.getPort(COLOUR_PORT));
        openSensors.add(sensor);

        return sensor;
    }

    public static EV3GyroSensor getGyroSensor(RemoteEV3 ev3){
        EV3GyroSensor sensor = new EV3GyroSensor(ev3.getPort(GYRO_PORT));
        openSensors.add(sensor);

        return sensor;
    }

    public static RMIRegulatedMotor getRightMotor(RemoteEV3 ev3){
        RMIRegulatedMotor motor = ev3.createRegulatedMotor(RIGHT_PORT, 'L');
        openMotors.add(motor);

        return motor;
    }

    public static RMIRegulatedMotor getLeftMotor(RemoteEV3 ev3){
        RMIRegulatedMotor motor = ev3.createRegulatedMotor(LEFT_PORT, 'L');
        openMotors.add(motor);

        return motor;
    }

    public static RMIRegulatedMotor getPanningMotor(RemoteEV3 ev3){
        RMIRegulatedMotor motor = ev3.createRegulatedMotor(PANNING_PORT, 'M');
        openMotors.add(motor);

        return motor;
    }

    public static void closeMotors(){
        for(RMIRegulatedMotor m : openMotors){
            try{
                m.close();
            }
            catch(RemoteException e){
                System.out.println("Failed to close motor port. Already closed?");
            }
        }
        openMotors.clear();
    }

    public static void closeSensors(){
        for(UARTSensor s : openSensors){
            s.close();
        }
        openSensors.clear();
    }

    public static void closeAllPorts(){
        closeMotors();
        closeSensors();
    }
    

    public static void main(String[] args){
        RemoteEV3 brick = findBrick("RescueBot");
        if(brick == null){
            return;
        }

        EV3UltrasonicSensor ultra = getUltrasonicSensor(brick);

        RMIRegulatedMotor m1 = getLeftMotor(brick);

        closeAllPorts();
    }
}