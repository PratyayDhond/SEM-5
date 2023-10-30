import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Server {
    private static final int SERVER_PORT = 12345;
    private static final double LOSS_PROBABILITY = 0.8;
    private static int expectedSeq = 0;

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
                    String seqNo = parts[2];

                    if (calculateChecksum(message).equals(checksum)) {
                        if (Integer.parseInt(seqNo) != expectedSeq % 2) {
                            System.out.println("Incorrect Sequence Number. Sending NAK.");
                            sendNAK(serverSocket, receivePacket.getAddress(), receivePacket.getPort());
                        } else {
                            System.out.println("Received message: \"" + message + "\". Sending ACK.");
                            sendACK(serverSocket, receivePacket.getAddress(), receivePacket.getPort());
                            expectedSeq++;
                        }
                    } else {
                        System.out.println("Received corrupted message. Sending NAK.");
                        sendNAK(serverSocket, receivePacket.getAddress(), receivePacket.getPort());
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

    private static void sendACK(DatagramSocket socket, InetAddress clientAddress, int clientPort) {
        String response = "ACK";
        String checksum = calculateChecksum(response);
        String packet = response + ":" + checksum;
        sendPacket(socket, packet, clientAddress, clientPort);
    }

    private static void sendNAK(DatagramSocket socket, InetAddress clientAddress, int clientPort) {
        String response = "NAK";
        String checksum = calculateChecksum(response);
        String packet = response + ":" + checksum;
        sendPacket(socket, packet, clientAddress, clientPort);
    }

    private static void sendPacket(DatagramSocket socket, String packet, InetAddress clientAddress, int clientPort) {
        try {
            byte[] sendData = packet.getBytes("UTF-8");
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
            socket.send(sendPacket);
        } catch (IOException e) {
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
