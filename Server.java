import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {

    private static ConcurrentHashMap<String, Socket> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(5000);
        System.out.println("Server running...");

        while (true) {
            Socket socket = server.accept();
            new Thread(() -> handleClient(socket)).start();
        }
    }

    private static ConcurrentHashMap<String, String> publicKeys = new ConcurrentHashMap<>();

    private static void handleClient(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String username = in.readLine();
            clients.put(username, socket);
            System.out.println(username + " connected");

            String line;
            while ((line = in.readLine()) != null) {

                if (line.startsWith("REGISTER")) {
                    String[] parts = line.split("\\|");
                    publicKeys.put(parts[1], parts[2]);
                    System.out.println(parts[1] + " registered key");
                }

                else if (line.startsWith("GETKEY")) {
                    String user = line.split("\\|")[1];
                    String key = publicKeys.get(user);
                    out.println(key == null ? "null" : key);
                }

                else {
                    // normal message forwarding
                    String[] parts = line.split("\\|", 2);
                    String recipient = parts[0];
                    String message = parts[1];

                    Socket target = clients.get(recipient);
                    if (target != null) {
                        PrintWriter targetOut = new PrintWriter(target.getOutputStream(), true);
                        targetOut.println(message);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}