/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class ChatClient {

    private static ChatClientSocket chatClientTCPSocket = null;
    private static DatagramSocket UDPSocket = null;
    private static final int SERVER_PORT = 8765;
    private static String MULTICAST_ADDR = "224.0.0.4";
    private static final int MULTICAST_PORT = 8767;
    private static String username = "dong";

    public static void startClient() {
        Scanner keyboardInputScanner = new Scanner(System.in);

        try {
            InetAddress multicastAddress = InetAddress.getByName(MULTICAST_ADDR);
            byte[] buf = new byte[256];
            MulticastSocket multicastSocket = new MulticastSocket(MULTICAST_PORT);
            multicastSocket.joinGroup(multicastAddress);
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
            // The receive method blocks until receiving a datagram packet
            multicastSocket.receive(datagramPacket);
            String msg = new String(buf, 0, buf.length);
            System.out.println("Received msg: " + msg);

        } catch (IOException e) {
            System.out.println("Client could not make connection: " + e.getMessage());
        }

        try {
            Message message = null;
            chatClientTCPSocket = new ChatClientSocket(InetAddress.getLocalHost(), SERVER_PORT);
            
            ObjectOutputStream oos = new ObjectOutputStream(chatClientTCPSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(chatClientTCPSocket.getInputStream());

            HandshakeMessage handshakeMessage = new HandshakeMessage("this is handshake message", username, InetAddress.getLocalHost());
            oos.writeObject(handshakeMessage);
            message = (Message) (ois.readObject());

            System.out.println("Connection made with " + chatClientTCPSocket.getInetAddress() + ":" + chatClientTCPSocket.getPort());

            System.out.println("Enter text or done to exit.");
            String clientRequest;

            do {
                clientRequest = keyboardInputScanner.nextLine();

                PrivateMessage pm = new PrivateMessage(clientRequest, username, InetAddress.getLocalHost());
                oos.writeObject(pm);

                message = (Message) (ois.readObject());
                System.out.println("Server response: " + message.getMessage());
            } while (!"done".equalsIgnoreCase(clientRequest.trim()));
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
