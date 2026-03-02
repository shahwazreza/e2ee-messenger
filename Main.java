import crypto.*;
import javax.crypto.SecretKey;
import java.security.KeyPair;

public class Main {
    public static void main(String[] args) throws Exception {

        // Simulate Alice & Bob
        KeyPair aliceKeys = KeyManager.generateKeyPair();
        KeyPair bobKeys = KeyManager.generateKeyPair();

        SecretKey aliceShared = KeyDerivation.deriveSharedKey(aliceKeys.getPrivate(), bobKeys.getPublic());

        SecretKey bobShared = KeyDerivation.deriveSharedKey(bobKeys.getPrivate(), aliceKeys.getPublic());

        var encrypted = Encryption.encrypt("Hello Bob!", aliceShared);
        String decrypted = Encryption.decrypt(encrypted, bobShared);

        System.out.println("Decrypted message: " + decrypted);
    }
}