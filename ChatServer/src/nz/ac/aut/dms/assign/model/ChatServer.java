package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class ChatServer {

    // Local tcp port listening for incoming requests
    public static final int SERVER_TCP_PORT = 8765;
    // The multicase ip address to which the server send broadcast messages
    public static String MULTICAST_ADDR = "224.0.0.4";
    // The multicast port to which the server send broadcast messages
    public static final int MULTICAST_PORT = 8767;
    // A boolean flag checking whether the server should continue running
    public static boolean stopServer = false;

    // The server socket which is used to create connections to the clients
    private ChatServerSocket chatServerSocket = null;
    // The datagram socket which is used to send broadcast messages
    private DatagramSocket datagramSocket = null;

    private HashMap<String, User> users = null;
    private ArrayList<Thread> threadPool = null;
    private Buffer buffer = null;

    public ChatServer(HashMap<String, User> users, ArrayList<Thread> threadPool, Buffer buffer) {
        this.users = users;
        this.threadPool = threadPool;
        this.buffer = buffer;
    }

    public void startServer() {
        // start a thread for broadcasting datagrams
        ServerMulticastSenderTask serverMulticastSenderTask = new ServerMulticastSenderTask(users, buffer);
        Thread serverMulticastSenderThread = new Thread(serverMulticastSenderTask);

        serverMulticastSenderThread.start();

        try {
            chatServerSocket = new ChatServerSocket(SERVER_TCP_PORT);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (!stopServer) {

            try {
                System.out.println("Server started at " + InetAddress.getLocalHost() + " on TCP port " + SERVER_TCP_PORT);

                Socket clientSocket = chatServerSocket.accept();

                ServerTCPReceiverTask serverTCPReceiverTask = new ServerTCPReceiverTask(clientSocket, users, buffer);
                ServerTCPSenderTask serverTCPSenderTask = new ServerTCPSenderTask(clientSocket, buffer);

                Thread serverTCPReceiverThread = new Thread(serverTCPReceiverTask);
                Thread serverTCPSenderThread = new Thread(serverTCPSenderTask);

                // start a tcp thread for receiving
                serverTCPReceiverThread.start();
                // start a tcp thread for sending
                serverTCPSenderThread.start();

                threadPool.add(serverTCPReceiverThread);
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
