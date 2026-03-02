package crypto;

import java.security.*;
import java.security.spec.NamedParameterSpec;

public class KeyManager {

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("X25519");
        kpg.initialize(new NamedParameterSpec("X25519"));
        return kpg.generateKeyPair();
    }
}