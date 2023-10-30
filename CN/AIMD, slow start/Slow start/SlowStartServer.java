import java.io.*;
import java.net.*;

public class SlowStartServer {
    public static void main(String[] args) {
        // Server configuration
        String serverHost = "127.0.0.1";    // local address
        int serverPort = 12345;             

        // Create a server socket
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort, 1, InetAddress.getByName(serverHost));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Server listening on " + serverHost + " port " + serverPort);

        // Accept a client connection
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Slow Start simulation
        int congestionWindow = 1;
        int threshold = 65535;
        try {
            while (true) {
                System.out.println("Server: Congestion window size = " + congestionWindow);

                // Send the congestion window value to the client
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(congestionWindow);

                // Receive ACK from the client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String ack = in.readLine();

                if (congestionWindow < threshold) {
                    congestionWindow *= 2;
                } else {
                    System.out.println("Threshold reached. Exiting Slow Start.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Slow Start phase finished.");

        try {
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
