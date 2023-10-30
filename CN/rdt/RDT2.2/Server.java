import java.io.IOException;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Server {
    private static final int SERVER_PORT = 12345;
    private static final double LOSS_PROBABILITY = 0.8;

    public static void main(String[] args) {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(SERVER_PORT);
            System.out.println("Server listening on localhost:" + SERVER_PORT);
            int expectedSeq = 0;

            while (true) {
                try {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);
                    String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    String[] parts = receivedMessage.split(":");
                    String message = parts[0];
                    String receivedChecksum = parts[1];
                    int receivedSeqNo = Integer.parseInt(parts[2]);

                    if (!calChecksum(message).equals(receivedChecksum)) {
                        System.out.println("Received corrupted message. Sending previous ACK: " + (expectedSeq - 1) % 2);
                        String checksum = calChecksum("ACK");
                        if (Math.random() > LOSS_PROBABILITY) {
                            checksum = calChecksum("ERROR");
                        }
                        String packet = "ACK:" + checksum + ":" + (expectedSeq - 1) % 2;
                        serverSocket.send(new DatagramPacket(packet.getBytes(), packet.length(), receivePacket.getAddress(), receivePacket.getPort()));
                    } else if (receivedSeqNo != expectedSeq % 2) {
                        System.out.println("Incorrect Sequence Number. Sending previous ACK: " + (expectedSeq - 1) % 2);
                        String checksum = calChecksum("ACK");
                        if (Math.random() > LOSS_PROBABILITY) {
                            checksum = calChecksum("ERROR");
                        }
                        String packet = "ACK:" + checksum + ":" + (expectedSeq - 1) % 2;
                        serverSocket.send(new DatagramPacket(packet.getBytes(), packet.length(), receivePacket.getAddress(), receivePacket.getPort()));
                    } else {
                        System.out.println("Received message: \"" + message + "\". Sending ACK: " + expectedSeq % 2);
                        String checksum = calChecksum("ACK");
                        String packet = "ACK:" + checksum + ":" + expectedSeq % 2;
                        serverSocket.send(new DatagramPacket(packet.getBytes(), packet.length(), receivePacket.getAddress(), receivePacket.getPort()));
                        expectedSeq++;
                    }

                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Connection terminated with " + receivePacket.getAddress() + ".");
                        break;
                    }
                } catch (IOException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        } catch (SocketException e) {
            System.err.println("SocketException: " + e.getMessage());
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        }
    }

    private static String calChecksum(String data) {
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
