import crypto.*;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.KeyPair;

public class Client {

    public static void main(String[] args) throws Exception {

        String username = args[0];
        String peer = args[1];

        Socket socket = new Socket("localhost", 5000);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println(username);

        // key exchange (for now simulated)
        KeyPair myKeys = KeyManager.generateKeyPair();
        String myPublicKey = java.util.Base64.getEncoder().encodeToString(myKeys.getPublic().getEncoded());

        out.println("REGISTER|" + username + "|" + myPublicKey);
        String peerKeyBase64 = null;

        while (peerKeyBase64 == null || peerKeyBase64.equals("null")) {
            out.println("GETKEY|" + peer);
            peerKeyBase64 = in.readLine();
            Thread.sleep(500); // wait half second before retry
        }

        byte[] peerBytes = java.util.Base64.getDecoder().decode(peerKeyBase64);

        java.security.KeyFactory kf = java.security.KeyFactory.getInstance("X25519");
        java.security.PublicKey peerPublic = kf.generatePublic(new java.security.spec.X509EncodedKeySpec(peerBytes));

        SecretKey shared = KeyDerivation.deriveSharedKey(myKeys.getPrivate(), peerPublic);
        System.out.println("Shared key: " + java.util.Base64.getEncoder().encodeToString(shared.getEncoded()));

        // receive thread
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println("Encrypted message received");

                    byte[] bytes = java.util.Base64.getDecoder().decode(msg);

                    Encryption.EncryptedData data = new Encryption.EncryptedData();
                    data.iv = java.util.Arrays.copyOfRange(bytes, 0, 12);
                    data.ciphertext = java.util.Arrays.copyOfRange(bytes, 12, bytes.length);

                    String decrypted = Encryption.decrypt(data, shared);
                    System.out.println("Received: " + decrypted);
                }
            } catch (Exception e) {
                System.out.println("Decryption failed: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String text = console.readLine();

            var encrypted = Encryption.encrypt(text, shared);

            byte[] combined = new byte[encrypted.iv.length + encrypted.ciphertext.length];
            System.arraycopy(encrypted.iv, 0, combined, 0, 12);
            System.arraycopy(encrypted.ciphertext, 0, combined, 12, encrypted.ciphertext.length);

            out.println(peer + "|" + java.util.Base64.getEncoder().encodeToString(combined));
        }
    }
}