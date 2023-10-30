import java.io.*;
import java.net.*;

public class SlowStartClient {
    public static void main(String[] args) {
        // Client configuration
        String serverHost = "127.0.0.1";
        int serverPort = 12345;

        // Create a client socket
        Socket clientSocket = null;
        try {
            clientSocket = new Socket(serverHost, serverPort);
            System.out.println("Connected to the server on " + serverHost + " port " + serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try {
            while (true) {
                // Receive data from the server
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String data = in.readLine();
                if (data == null) {
                    break;
                }

                System.out.println("Client received data size : " + data);

                // Simulate an ACK by sending an "ACK" response to the server
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("ACK");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
