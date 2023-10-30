import java.io.*;
import java.net.*;

public class TcpRenoServer {
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
            int ssthresh = 128;
            int noOfPacketsToSend = 2048;
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
                    if (ack.contains("FAST RECOVERY")){
                        System.out.println("Entering Fast recovery mode...");
                        cwnd = cwnd/2 + 3; // +3 for the three duplicates received
                        try{
                            int duplicates = Integer.parseInt(ack.substring(13));
                            cwnd += duplicates;
                        }catch(Exception e){}
                    }
                    else if (cwnd <= ssthresh) {
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
