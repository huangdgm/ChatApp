package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class ChatServer {

    private static final int SERVER_TCP_PORT = 8765;
    private static final int MULTICAST_PORT = 8767;
    private static ChatServerSocket chatServerSocket = null;
    private static DatagramSocket UDPSocket = null;
    private static String MULTICAST_ADDR = "224.0.0.4";
    private static boolean stopServer = false;

    public static void startServer() {

        try {
            chatServerSocket = new ChatServerSocket(SERVER_TCP_PORT);
            System.out.println("Server started at " + InetAddress.getLocalHost() + " on TCP port " + SERVER_TCP_PORT);

            InetAddress multicastAddress = InetAddress.getByName(MULTICAST_ADDR);
            UDPSocket = new DatagramSocket();

            String broadcastMessage = "new user dong connected.";
            DatagramPacket broadcastDatagramPacket = new DatagramPacket(broadcastMessage.getBytes(),broadcastMessage.getBytes().length, multicastAddress, MULTICAST_PORT);
            UDPSocket.send(broadcastDatagramPacket);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        while (!stopServer) {
            try {
                // create a seperate UDPSocket to connect to the client
                Socket clientSocket = chatServerSocket.accept();

                System.out.println("Connection made with " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());

                Connection connection = new Connection(clientSocket);

                connection.start();

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
