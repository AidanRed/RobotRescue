import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Fingerprint {
    private byte[] fingerprint;

    public Fingerprint(){
        fingerprint = generateFingerprint();
    }

    public byte[] getFingerprint(){
        return fingerprint;
    }

    private String findData(){
        //getting robot info
        String toHash = getDeviceSignature(true);
        return toHash;
    }

    private byte[] generateFingerprint(){
        String toHash = findData();
        MessageDigest digest;
        try{
            digest = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) {
            digest = null;
        }
        byte[] fp = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));
        //do hashing here and return
        return fp;
    }

    public static void main(String[] args){
        Fingerprint myFp = new Fingerprint();
        System.out.println(Arrays.toString(myFp.getFingerprint()));
    }
}