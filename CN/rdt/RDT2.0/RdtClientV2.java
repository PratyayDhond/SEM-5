import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class RdtClientV2 {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final double LOSS_PROBABILITY = 0.75;

    public static void main(String[] args) {
        try {
            DatagramSocket clientSocket = new DatagramSocket();

            while (true) {
                try {
                    BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                    System.out.print("Client: Enter your message (or 'exit' to quit): ");
                    String message = userInput.readLine();

                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }

                    String checksum = calculateChecksum(message);
                    String oldMessage = message;

                    if (Math.random() > LOSS_PROBABILITY) {
                        message = "Error";
                    }

                    String packet = message + ":" + checksum;
                    byte[] sendData = packet.getBytes("UTF-8");

                    InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);

                    clientSocket.send(sendPacket);

                    boolean ackReceived = false;

                    while (!ackReceived) {
                        byte[] receiveData = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivePacket);

                        String response = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");

                        if (response.equals("ACK")) {
                            System.out.println("Received ACK");
                            ackReceived = true;
                        } else {
                            System.out.println("Received NAK, resending the message...");
                            if (Math.random() < LOSS_PROBABILITY) {
                                message = oldMessage;
                                packet = message + ":" + checksum;
                                sendData = packet.getBytes("UTF-8");
                                sendPacket.setData(sendData);
                                clientSocket.send(sendPacket);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String calculateChecksum(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes());
            byte[] digest = md.digest();

            StringBuilder checksum = new StringBuilder();
            for (byte b : digest) {
                checksum.append(String.format("%02x", b));
            }

            return checksum.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}
