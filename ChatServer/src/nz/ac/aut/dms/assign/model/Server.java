package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class Server {

    public static Status serverStatus;

    private ServerSocket serverSocket;
    private final Buffer buffer;

    private final ArrayList<Socket> clientSockets;

    public Server() {
        serverStatus = Status.OFFLINE;
        buffer = new Buffer();
        clientSockets = new ArrayList<>();
    }

    /**
     * Start a server means two things: 1. Start a separate thread for
     * broadcasting datagram packets. 2. Start listening TCP connection
     * requests.
     */
    public void start() {
        serverStatus = Status.ONLINE;

        // Start a separate thread for broadcasting datagram packets
        Thread multicastThread = new Thread(new MulticastTask(buffer));
        multicastThread.start();

        // Open the ServerSocket
        try {
            serverSocket = new ServerSocket(ServerConfig.SERVER_TCP_PORT);
            System.out.println("Info: ServerSocket started.");
        } catch (IOException ex) {
            System.out.println("Error: error starting the server socket. " + ex.getMessage());
            serverStatus = serverStatus.OFFLINE;
        }

        // Start listening for the incoming connection requests
        while (serverStatus == Status.ONLINE) {
            Socket clientSocket = null;

            try {
                clientSocket = serverSocket.accept();
                System.out.println("Info: connection successfully established with: " + clientSocket);
            } catch (IOException e) {
                System.out.println("Error: can not accept client connection: " + e.getMessage());
                serverStatus = Status.OFFLINE;
            }

            clientSockets.add(clientSocket);

            String remoteClientName = new String(); // shared by the sender thread and the receiver thread

            // Create threads for each connection
            Thread tcpSenderThread = new Thread(new TCPSenderTask(clientSocket, buffer, remoteClientName));
            Thread tcpReceiverThread = new Thread(new TCPReceiverTask(clientSocket, buffer, remoteClientName));

            tcpSenderThread.start();
            tcpReceiverThread.start();
        }

        // Close the ServerSocket and all the Socket
        try {
            serverSocket.close();

            for (Socket clientSocket : clientSockets) {
                clientSocket.close();
            }

            System.out.println("Info: server socket and all client sockets closed.");
        } catch (IOException ex) {
            System.out.println("Error: ServerSocket not successfully closed. " + ex.getMessage());
        } finally {
            serverStatus = Status.OFFLINE;
        }
    }
}
