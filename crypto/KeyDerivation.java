package crypto;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class KeyDerivation {

    public static SecretKey deriveSharedKey(PrivateKey myPrivate, PublicKey theirPublic) throws Exception {

        KeyAgreement ka = KeyAgreement.getInstance("X25519");
        ka.init(myPrivate);
        ka.doPhase(theirPublic, true);

        byte[] sharedSecret = ka.generateSecret();

        return new SecretKeySpec(sharedSecret, 0, 16, "AES");
    }
}