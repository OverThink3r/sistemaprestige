package objetos;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class encrypt {
    public encrypt(){
    }

    public byte[] encpass(String pass) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), salt, 65536, 128);
        try {
            
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            
            byte[] combined = Arrays.copyOf(salt, hash.length + salt.length);
            
            System.arraycopy(hash, 0, combined, salt.length, hash.length);

            return combined;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return salt;
    }

    public boolean verifypass(byte[] combined, String rpass) {
        
        byte[] compareHash = Arrays.copyOfRange(combined, 16, combined.length);
        byte[] secondSalt = Arrays.copyOf(combined, 16);
        
        try {
            KeySpec spec = new PBEKeySpec(rpass.toCharArray(), secondSalt, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] secondHash = factory.generateSecret(spec).getEncoded();
            if(Arrays.equals(compareHash, secondHash)){
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
