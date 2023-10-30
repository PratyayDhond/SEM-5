import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Server {
    public static void main(String[] args) {
        DatagramSocket receiverSocket = null;
        try {
            receiverSocket = new DatagramSocket(12345, InetAddress.getByName("127.0.0.1"));

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                receiverSocket.receive(receivePacket);

                String receivedMessage = new String(receivePacket.getData(), 0, receivePacket.getLength());
                InetAddress senderAddress = receivePacket.getAddress();
                int senderPort = receivePacket.getPort();

                String responseMessage = "ACK";
                byte[] responseData = responseMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(responseData, responseData.length, senderAddress, senderPort);
                receiverSocket.send(sendPacket);

                System.out.println("Received: " + receivedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (receiverSocket != null && !receiverSocket.isClosed()) {
                receiverSocket.close();
            }
        }
    }
}
