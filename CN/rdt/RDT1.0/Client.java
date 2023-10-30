import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        DatagramSocket senderSocket = null;
        try {
            senderSocket = new DatagramSocket();

            InetAddress receiverAddress = InetAddress.getByName("127.0.0.1");
            int receiverPort = 12345;

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Enter a message to send: ");
                String message = scanner.nextLine();

                if(message.equalsIgnoreCase("#exit")){
                    System.out.println("Exiting chat room. Have a good day...");
                    return;
                }

                byte[] messageData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(messageData, messageData.length, receiverAddress, receiverPort);
                senderSocket.send(sendPacket);

                byte[] ackData = new byte[3];
                DatagramPacket receiveAckPacket = new DatagramPacket(ackData, ackData.length);
                senderSocket.receive(receiveAckPacket);

                String ack = new String(receiveAckPacket.getData(), 0, receiveAckPacket.getLength());
                if (ack.equals("ACK")) {
                    System.out.println("Message sent successfully.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (senderSocket != null && !senderSocket.isClosed()) {
                senderSocket.close();
            }
        }
    }
}
