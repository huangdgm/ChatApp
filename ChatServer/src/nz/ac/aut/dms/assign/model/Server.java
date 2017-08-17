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

    public static ServerStatus serverStatus;

    private ServerSocket serverSocket;
    private Buffer buffer;
    
    private ArrayList<Socket> clientSockets;

    public Server() {
        serverStatus = ServerStatus.OFFLINE;
        buffer = new Buffer();
        clientSockets = new ArrayList<>();
    }

    /**
     * Start a server means two things: 1. Start a separate thread for
     * broadcasting datagram packets. 2. Start listening TCP connection
     * requests.
     */
    public void start() {
        serverStatus = ServerStatus.ONLINE;

        // Start a separate thread for broadcasting datagram packets
        Thread multicastThread = new Thread(new MulticastTask(buffer));
        multicastThread.start();

        // Open the ServerSocket
        try {
            serverSocket = new ServerSocket(ServerConfig.SERVER_TCP_PORT);
            System.out.println("Info: ServerSocket started: " + serverSocket);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: ServerSocket not started!");
            serverStatus = serverStatus.OFFLINE;
        }

        // Start listening for the incoming connection requests
        while (serverStatus == ServerStatus.ONLINE) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientSockets.add(clientSocket);

                Thread tcpThread = new Thread(new TCPTask(clientSocket, buffer));
                tcpThread.start();
            } catch (IOException e) {
                System.out.println("Can not accept client connection: " + e.getMessage());
                serverStatus = ServerStatus.OFFLINE;
            }
        }
        
        // Close the ServerSocket and all the Socket
        try {
            serverSocket.close();

            for (Socket clientSocket : clientSockets) {
                clientSocket.close();
            }

            System.out.println("Info: ServerSocket closed.");
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: ServerSocket not successfully closed.");
        } finally {
            serverStatus = ServerStatus.OFFLINE;
        }
    }
}
