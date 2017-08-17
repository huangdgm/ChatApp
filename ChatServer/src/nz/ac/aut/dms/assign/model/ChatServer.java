package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class ChatServer {

    public static final int SERVER_TCP_PORT = 8765;
    public static String MULTICAST_ADDR = "224.0.0.4";
    public static final int MULTICAST_PORT = 8767;
    public static boolean stopServer = false;

    private ServerSocket serverSocket = null;
    private DatagramSocket datagramSocket = null;

    private ArrayList<User> users = null;
    private ArrayList<Message> messages = null;

    public ChatServer(ArrayList<User> users, ArrayList<Message> messages) {
        this.users = users;
        this.messages = messages;
    }

    public void startServer() {
        // start a thread for broadcasting datagrams
        ServerMulticastSenderTask serverMulticastSenderTask = new ServerMulticastSenderTask(users);
        Thread serverMulticastSenderThread = new Thread(serverMulticastSenderTask);

        serverMulticastSenderThread.start();

        try {
            serverSocket = new ServerSocket(SERVER_TCP_PORT);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        Socket clientSocket = null;

        while (!stopServer) {

            try {
                System.out.println("Server started at " + InetAddress.getLocalHost() + " on TCP port " + SERVER_TCP_PORT);

                clientSocket = serverSocket.accept();

                ServerTCPSenderTask serverTCPSenderTask = new ServerTCPSenderTask(clientSocket, users, messages);
                ServerTCPReceiverTask serverTCPReceiverTask = new ServerTCPReceiverTask(clientSocket, users, messages);

                Thread serverTCPSenderThread = new Thread(serverTCPSenderTask);
                Thread serverTCPReceiverThread = new Thread(serverTCPReceiverTask);

                // start a tcp thread for sending
                serverTCPSenderThread.start();
                // start a tcp thread for receiving
                serverTCPReceiverThread.start();
            } catch (IOException e) {
                System.err.println("Can not accept client connection: " + e.getMessage());
                stopServer = true;
            }
        }

        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Server stopped.");
    }
}
