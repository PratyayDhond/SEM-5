import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class Client {
    private static final int SERVER_PORT = 12345;
    private static final int TIMEOUT = 5000;

    private static DatagramSocket socket;
    private static InetAddress serverAddress;
    private static int sequenceNumber = 0;
    private static int expectedAck = 0;

    public static void main(String[] args) {
        try {
            socket = new DatagramSocket();
            serverAddress = InetAddress.getByName("localhost");

            // Set up a timer to handle timeouts
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        // Send a dummy message to maintain the connection
                        sendData("Ping");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, TIMEOUT, TIMEOUT);

            // Start a thread to receive messages from the server
            Thread receiveThread = new Thread(() -> {
                while (true) {
                    try {
                        receiveAck();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            receiveThread.start();

            // Read and send user messages
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String message = reader.readLine();
                sendData(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendData(String message) throws IOException {
        String packet = sequenceNumber + ":" + message;
        DatagramPacket dataPacket = new DatagramPacket(packet.getBytes(), packet.length(), serverAddress, SERVER_PORT);
        socket.send(dataPacket);
        System.out.println("Sent: " + packet);
    }

    private static void receiveAck() throws IOException {
        byte[] receiveData = new byte[1024];
        DatagramPacket ackPacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(ackPacket);

        // Simulate corrupted acknowledgments and incorrect sequence numbers
        String ackMessage = new String(ackPacket.getData(), 0, ackPacket.getLength());
        if (new Random().nextInt(10) < 3)  {
            // Simulate a corrupted acknowledgment
            ackMessage = "CorruptedAck";
        } else {
            // Simulate incorrect sequence numbers
            int ack = Integer.parseInt(ackMessage);
            if (ack == expectedAck) {
                expectedAck = 1 - expectedAck;
            } else {
                ackMessage = "IncorrectSeq";
            }
        }

        System.out.println("Received Ack: " + ackMessage);
    }
}
