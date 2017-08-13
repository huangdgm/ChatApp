package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class ChatServer {

    // Local tcp port listening for incoming requests
    private static final int SERVER_TCP_PORT = 8765;
    // The multicase ip address to which the server send broadcast messages
    private static String MULTICAST_ADDR = "224.0.0.4";
    // The multicast port to which the server send broadcast messages
    private static final int MULTICAST_PORT = 8767;
    // The server socket which is used to create connections to the clients
    private static ChatServerSocket chatServerSocket = null;
    // The datagram socket which is used to send broadcast messages
    private static DatagramSocket datagramSocket = null;
    // A boolean flag checking whether the server should continue running
    private static boolean stopServer = false;
    
    private static ArrayList<User> users = null;

    public static void startServer() {
        users = new ArrayList<User>();

        while (!stopServer) {
            try {
                chatServerSocket = new ChatServerSocket(SERVER_TCP_PORT);
                System.out.println("Server started at " + InetAddress.getLocalHost() + " on TCP port " + SERVER_TCP_PORT);

                InetAddress multicastAddress = InetAddress.getByName(MULTICAST_ADDR);
                datagramSocket = new DatagramSocket();

                Message message = new BroadcastMessage("user dong connected", "dong", multicastAddress);
                DatagramPacket broadcastDatagramPacket = new DatagramPacket(message.getMessage().getBytes(), message.getMessage().getBytes().length, multicastAddress, MULTICAST_PORT);
                datagramSocket.send(broadcastDatagramPacket);
                Thread.sleep(2000);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                // create a seperate datagramSocket to connect to the client
                Socket clientSocket = chatServerSocket.accept();
                
                System.out.println("Connection made with " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                ChatServerThread chatServerThread = new ChatServerThread(clientSocket);

                chatServerThread.start();

                System.out.println("Processed the client and started connection");
            } catch (IOException e) {
                System.err.println("Can not accept client connection: " + e.getMessage());
                stopServer = true;
            }
        }

        try {
            chatServerSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Server finishing");
    }
}
