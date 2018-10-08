import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.device.DeviceIdentifier;
import java.util.Scanner;
import lejos.hardware.port.SensorPort;
import lejos.hardware.port.MotorPort;

// class for creating and storing a unique digital fingerprint 
// to identify the robot
public class Fingerprint {
    private byte[] fingerprint;
    //construct a fingerprint
    public Fingerprint(){
        fingerprint = generateFingerprint();
    }

    public byte[] getFingerprint(){
        return fingerprint;
    }
    // find unique device data and concatinate it into one string
    private String findData(){
        //getting robot info
        String toHash = "";
        //add device information for each port
//        String[] portNames = {"S1", "S2", "S3", "S4", "A", "B", "C", "D"};
        Port[] port= {SensorPort.S1, SensorPort.S2, SensorPort.S3, SensorPort.S4, 
                MotorPort.A, MotorPort.B, MotorPort.C, MotorPort.D};
        for(int i = 0; i < port.length; i++){
//            Port p = LocalEV3.get().getPort(portNames[i]);
            DeviceIdentifier devId = new DeviceIdentifier(port[i]);
            toHash += devId.getDeviceSignature(true);
            devId.close();
        }
        
        // add more EV3 info here

        return toHash;
    }
    // hash robot data into a byte array which is a fingerprint
    private byte[] generateFingerprint(){
        String toHash = findData();
        MessageDigest digest;
        try{
            digest = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            digest = null;
        }
        byte[] fp = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));

        return fp;
    }
    //this is just for testing and will be removed
    public static void main(String[] args){
        Fingerprint myFp = new Fingerprint();
        byte[] first = myFp.getFingerprint();
//        String first = myFp.findData();

        // Wait for an input
//        Scanner reader = new Scanner(System.in);
        System.out.println("You have aproximately 5 seconds to modify the robot.");
        try {
            Thread.sleep(5000);
        }
        catch(Exception e) {
            System.out.println("It messed up");
        }
//        reader.nextLine();
//        reader.close();
        
        // Output results
        Fingerprint myFp2 = new Fingerprint();
        byte[] second = myFp2.getFingerprint();
        System.out.println("First = " + Arrays.toString(first));
        System.out.println("Second = " + Arrays.toString(second));
//      String second = myFp.findData();
//      System.out.println("First = " + first);
//      System.out.println("Second = " + second);
        
        // Compare fingerprints
//        if(!first.equals(second)) {
        if(!Arrays.equals(first, second)) {
            System.out.println("Fingerprint has changed!");
        } else {
            System.out.println("Fingerprint is the same!");
        }
        try {
            Thread.sleep(10000);
        }
        catch(Exception e) {
            System.out.println("It messed up");
        }
    }
}