import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final double LOSS_PROBABILITY = 0.75;
    private static int SEQ_NO = 0;

    public static void main(String[] args) {
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();

            while (true) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                    System.out.print("Client: Enter your message (or 'exit' to quit): ");
                    String message = reader.readLine();

                    if (message.equalsIgnoreCase("exit")) {
                        break;
                    }

                    String checksum = calculateChecksum(message);
                    String oldMessage = message;

                    if (Math.random() > LOSS_PROBABILITY) {
                        message = "Error";
                    }

                    String packet = message + ":" + checksum + ":" + SEQ_NO % 2;

                    if (Math.random() > LOSS_PROBABILITY) {
                        packet = message + ":" + checksum + ":" + (SEQ_NO % 2 == 0 ? 1 : 0);
                    }
                    byte[] sendData = packet.getBytes();
                    InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
                    clientSocket.send(sendPacket);

                    boolean ackReceived = false;
                    while (!ackReceived) {
                        byte[] receiveData = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivePacket);
                        String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        String[] parts = response.split(":");
                        String receivedChecksum = parts[1];
                        int receivedSeqNo = Integer.parseInt(parts[2]);

                        if (receivedSeqNo != SEQ_NO % 2) {
                            System.out.println("Received incorrect ACK, resending the message...");
                            if (Math.random() < LOSS_PROBABILITY) {
                                message = oldMessage;
                            }
                            checksum = calculateChecksum(message);
                            packet = message + ":" + checksum + ":" + (SEQ_NO % 2);
                            sendData = packet.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
                            clientSocket.send(sendPacket);
                        } else if (receivedChecksum.equals(checksum)) {
                            System.out.println("Received corrupted response, resending the message...");
                            if (Math.random() < LOSS_PROBABILITY) {
                                message = oldMessage;
                            }
                            checksum = calculateChecksum(message);
                            packet = message + ":" + checksum + ":" + (SEQ_NO % 2);
                            sendData = packet.getBytes();
                            sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, SERVER_PORT);
                            clientSocket.send(sendPacket);
                        } else {
                            System.out.println("Received ACK");
                            ackReceived = true;
                            SEQ_NO++;
                        }

                    }

                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                }

            }
        } catch (SocketException e) {
            System.err.println("SocketException: " + e.getMessage());
        } finally {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        }
    }

    private static String calculateChecksum(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data.getBytes());
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
