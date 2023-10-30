

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RdtServerV2 {
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);

            System.out.println("Server listening on localhost:" + SERVER_PORT);

            while (true) {
                try {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    serverSocket.receive(receivePacket);

                    String data = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
                    String[] parts = data.split(":");
                    String message = parts[0];
                    String checksum = parts[1];

                    if (calculateChecksum(message).equals(checksum)) {
                        System.out.println("Received message: \"" + message + "\". Sending ACK.");
                        byte[] ackData = "ACK".getBytes("UTF-8");
                        InetAddress clientAddress = receivePacket.getAddress();
                        int clientPort = receivePacket.getPort();
                        DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, clientAddress, clientPort);
                        serverSocket.send(ackPacket);
                    } else {
                        System.out.println("Received corrupted message. Sending NAK.");
                        byte[] nakData = "NAK".getBytes("UTF-8");
                        InetAddress clientAddress = receivePacket.getAddress();
                        int clientPort = receivePacket.getPort();
                        DatagramPacket nakPacket = new DatagramPacket(nakData, nakData.length, clientAddress, clientPort);
                        serverSocket.send(nakPacket);
                    }

                    if (message.equalsIgnoreCase("exit")) {
                        System.out.println("Connection terminated with " + receivePacket.getAddress() + ".");
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            serverSocket.close();
        } catch (SocketException e) {
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
