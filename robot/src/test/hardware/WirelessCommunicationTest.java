/*
 * Written by Abdul Mohsi Jawaid
 * Testing wireless communication 
 * with the EV3 brick
 */


import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lejos.hardware.Audio;
import lejos.remote.ev3.RemoteEV3;

public class WirelessCommunicationTest {

    public static void main(String[] args) {
        // play beep if connected to robot
        RemoteEV3 ev3 = RobotUtility.findBrick();
    }

}
