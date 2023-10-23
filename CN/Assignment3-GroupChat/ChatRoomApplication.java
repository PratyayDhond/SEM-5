
import java.net.*;
import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;  
import java.time.LocalDateTime; 
   
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
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
                LocalDateTime now = LocalDateTime.now();  
                System.out.println("Enter `#exit` to terminate conversation | Start typing messages...\n");
                if(name.equalsIgnoreCase("Server"))
                    System.out.println("Enter `kick {username} to remove user from the group");
                else{
                    String message = "`" + name + "` has joined the chat!";
                    byte[] buffer = message.getBytes();
                    DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,multicastGroup,portNumber);
                    socket.send(datagram);
                }
                while(true)
                {
                    String message;
                    message = sc.nextLine();
                    now = LocalDateTime.now();
                    if(message.equalsIgnoreCase(ChatRoomApplication.EXIT)) // CHECK EXIT CONDITION
                    {
                        finished = true;
                        message = "[" + dtf.format(now) + "] " + "--- " + name + " has left the chat. ---";
                         byte[] buffer = message.getBytes();
                        DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,multicastGroup,portNumber);
                        socket.send(datagram);
                        socket.leaveGroup(multicastGroup);
                        socket.close();
                        System.out.println("You have successfully left the chatroom. Have a great day.");
                        break;
                    }
                    message = "[" + dtf.format(now) + "] " + name + ": " + message;
                    byte[] buffer = message.getBytes();
                    DatagramPacket datagram = new DatagramPacket(buffer,buffer.length,multicastGroup,portNumber);
                    socket.send(datagram);
                }
            }
            catch(SocketException se)
            {
                System.out.println("Error creating socket");
                // se.printStackTrace();
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        while(!ChatRoomApplication.finished)
        {
                byte[] buffer = new byte[ReadMessagesThread.MESSAGE_LENGTH];
                DatagramPacket datagram = new
                DatagramPacket(buffer,buffer.length,multicastGroup,portNumber);
                String message;
                now = LocalDateTime.now();
            try
            {
                socket.receive(datagram);
                message = new String(buffer,0,datagram.getLength(),"UTF-8");
                // System.out.println(ChatRoomApplication.name);
                if(message.substring(11).equalsIgnoreCase("server: kick " + ChatRoomApplication.name)){
                    socket.leaveGroup(multicastGroup);
                    socket.close();
                    System.out.println("You have been kicked out from the server.");
                    System.exit(0);
                    return;
                }
                if(message.substring(11,11+12).equalsIgnoreCase("server: kick")){
                    continue;   
                }

                if(message.contains("` has joined the chat")){
                    System.out.println("----" + message + "----"); 
                    continue;                    
                }
                if(!(message.contains(ChatRoomApplication.name + ":") || message.contains("--- " + ChatRoomApplication.name)))
                        System.out.println("[" + dtf.format(now) + "] "+ message);
            }
            catch(IOException e)
            {
                System.out.println("Socket closed!");
            }
        }
    }
}