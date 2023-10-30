import java.io.*;
import java.net.*;

public class TcpTahoeServer {
    public static void main(String[] args) {
        // Server configuration
        String serverHost = "127.0.0.1";
        int serverPort = 12345;

        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(serverPort, 1, InetAddress.getByName(serverHost));
            System.out.println("Server listening on " + serverHost + " port " + serverPort);

            // Accept a client connection
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket.getInetAddress());

            // Initialize TCP Tahoe parameters
            int cwnd = 1;
            int ssthresh = 64;
            int noOfPacketsToSend = 1000;
            int packetsSent = 0;
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (noOfPacketsToSend > packetsSent) {
                    // Send data to the client
                    System.out.println("Server: Congestion window size = " + cwnd + " | No of packets to send = " + noOfPacketsToSend);
                    out.println(cwnd);

                    // Simulate network delay
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Receive acknowledgment from the client
                    String ack = in.readLine();
                    noOfPacketsToSend -= cwnd;
                    if (cwnd <= ssthresh) {
                        // Slow start phase: Increase cwnd exponentially
                        cwnd *= 2;
                    } else {
                        // Congestion avoidance phase: Increase cwnd linearly
                        cwnd++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Server finished.");
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
