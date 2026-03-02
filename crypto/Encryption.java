package crypto;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;

public class Encryption {

    public static class EncryptedData {
        public byte[] iv;
        public byte[] ciphertext;
    }

    public static EncryptedData encrypt(String message, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));

        EncryptedData data = new EncryptedData();
        data.iv = iv;
        data.ciphertext = cipher.doFinal(message.getBytes());

        return data;
    }

    public static String decrypt(EncryptedData data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, data.iv));

        byte[] plaintext = cipher.doFinal(data.ciphertext);
        return new String(plaintext);
    }
}