
import java.net.*;
import java.io.*;
import java.util.*;
public class ChatRoomApplication
{
    static final String EXIT = "#EXIT";
    static String name; // static as it would be needed only once for every running instance, i.e. for current user name
    static volatile boolean finished = false;   // volatile as the boolean finished may be updated by any thread, the listener thread or sending thread
    public static void main(String[] args)
    {   
        if (args.length != 2){
            System.out.println("Two arguments required: {Multicast host} {Port-number}");
            System.out.println("Multicast Host - Multicast IP address which falls in class D | Range : 224.0.0.0 to 239.255.255.255");
        }
        else
        {
            try
            {
                InetAddress multicastGroup = InetAddress.getByName(args[0]);
                int portNumber = Integer.parseInt(args[1]);
                Scanner sc = new Scanner(System.in);
                System.out.print("Enter your name: ");
                name = sc.next();
                sc.nextLine();
                MulticastSocket socket = new MulticastSocket(portNumber);
              
                socket.setTimeToLive(0);    // setting ttl to 0 as no router hops will be there since running on localhost
                socket.joinGroup(multicastGroup);   // joining the user specified multicast IP address
                Thread t = new Thread(new ReadMessagesThread(socket,multicastGroup,portNumber));
              
                t.start(); 
                System.out.println("Enter `#exit` to terminate conversation | Start typing messages...\n");
                while(true)
                {
                    String message;
                    message = sc.nextLine();
                    if(message.equalsIgnoreCase(ChatRoomApplication.EXIT)) // CHECK EXIT CONDITION
                    {
                        finished = true;
                        message = "--- " + name + " has left the chat. ---";
                         byte[] buffer = message.getBytes();
                        DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,multicastGroup,portNumber);
                        socket.send(datagram);
                        socket.leaveGroup(multicastGroup);
                        socket.close();
                        System.out.println("You have successfully left the chatroom. Have a great day.");
                        break;
                    }
                    message = name + ": " + message;
                    byte[] buffer = message.getBytes();
                    DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,multicastGroup,portNumber);
                    socket.send(datagram);
                }
            }
            catch(SocketException se)
            {
                System.out.println("Error creating socket");
                se.printStackTrace();
            }
            catch(IOException ie)
            {
                System.out.println("Error reading/writing from/to socket");
                ie.printStackTrace();
            }
        }
    }
}
class ReadMessagesThread implements Runnable
{
    private MulticastSocket socket;
    private InetAddress multicastGroup;
    private int portNumber;
    private static final int MESSAGE_LENGTH = 1024;
    ReadMessagesThread(MulticastSocket socket,InetAddress multicastGroup,int portNumber)
    {
        this.socket = socket;
        this.multicastGroup = multicastGroup;
        this.portNumber = portNumber;
    }
      
    @Override
    public void run()
    {
        while(!ChatRoomApplication.finished)
        {
                byte[] buffer = new byte[ReadMessagesThread.MESSAGE_LENGTH];
                DatagramPacket datagram = new
                DatagramPacket(buffer,buffer.length,multicastGroup,portNumber);
                String message;
            try
            {
                socket.receive(datagram);
                message = new String(buffer,0,datagram.getLength(),"UTF-8");
                if(!(message.startsWith(ChatRoomApplication.name) || message.startsWith("--- " + ChatRoomApplication.name)))
                        System.out.println(message);
            }
            catch(IOException e)
            {
                System.out.println("Socket closed!");
            }
        }
    }
}