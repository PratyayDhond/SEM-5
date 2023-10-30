import java.io.*;
import java.net.*;

public class Server {
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(SERVER_PORT);
            System.out.println("Server is running...");

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                socket.receive(receivePacket);

                String data = new String(receivePacket.getData(), 0, receivePacket.getLength());
                String[] parts = data.split(":");
                int sequenceNumber = Integer.parseInt(parts[0]);

                if (sequenceNumber == 0) {
                    System.out.println("Received: " + parts[1]);
                    sendAck(0, receivePacket.getAddress(), receivePacket.getPort(), socket);
                } else {
                    // Simulate a packet loss by not sending an acknowledgment
                    System.out.println("Packet loss: " + parts[1]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private static void sendAck(int ack, InetAddress clientAddress, int clientPort, DatagramSocket socket) throws IOException {
        String ackMessage = Integer.toString(ack);
        DatagramPacket ackPacket = new DatagramPacket(ackMessage.getBytes(), ackMessage.length(), clientAddress, clientPort);
        socket.send(ackPacket);
        System.out.println("Sent Ack: " + ack);
    }
}
