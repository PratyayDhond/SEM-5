import java.io.*;
import java.net.*;
import java.util.Random;

public class TcpRenoClient {
    public static void main(String[] args) {
        // Client configuration
        String serverHost = "127.0.0.1";
        int serverPort = 12345;

        try {
            // Create a client socket
            Socket clientSocket = new Socket(serverHost, serverPort);
            System.out.println("Connected to the server on " + serverHost + " port " + serverPort);

            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                while (true) {
                    // Receive data from the server
                    String data = in.readLine();
                    if (data == null) {
                        break;
                    }

                    Random rand = new Random();
                    System.out.println("Client received data till : " + data);
                    int threeDuplicates = rand.nextInt(100);
                    if(threeDuplicates > 30){
                        // Simulate an ACK by sending an "ACK" response to the server
                        out.println("ACK");
                    }else{
                        // Fast recovery.
                        int duplicateACKS = rand.nextInt(10);
                        out.println("FAST RECOVERY " + duplicateACKS);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
