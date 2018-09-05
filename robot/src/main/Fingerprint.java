import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.ev3.Port;
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
        String[] portNames = {"S1", "S2", "S3", "S4", "A", "B", "C", "D"};
        for(int i = 0; i < 8; i++){
            Port p = LocalEV3.get().getPort(portNames[i]);
            DeviceIdentifier devId = new DeviceIdentifier(p);
            toHash += devId.getDeviceSignature(true);
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
        System.out.println(Arrays.toString(myFp.getFingerprint()));
    }
}