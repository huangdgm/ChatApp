/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class ChatClient {

    public static final String MULTICAST_ADDR = "224.0.0.4";
    public static final int MULTICAST_PORT = 8767;
    public static final int SERVER_PORT = 8765;
    public static boolean stopClient = false;

    private ChatClientSocket chatClientTCPSocket = null;

    public ChatClient() {
        try {
            chatClientTCPSocket = new ChatClientSocket(InetAddress.getLocalHost(), SERVER_PORT);
        } catch (UnknownHostException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startClient() {
        // start udp thread for receiving broadcast messages from the server
        ClientMulticastReceiverTask clientMulticastReceiverTask = new ClientMulticastReceiverTask();
        Thread clientMulticastReceiverThread = new Thread(clientMulticastReceiverTask);
        clientMulticastReceiverThread.start();

        // start tcp thread for sending unicast messages to the server
        ClientTCPSenderTask clientTCPConnectionTask = new ClientTCPSenderTask(chatClientTCPSocket);
        Thread clientTCPSenderThread = new Thread(clientTCPConnectionTask);
        clientTCPSenderThread.start();

        // start tcp thread for receiving unicast messages from the server
        ClientTCPReceiverTask clientTCPReceiverTask = new ClientTCPReceiverTask(chatClientTCPSocket);
        Thread clientTCPReceiverThread = new Thread(clientTCPReceiverTask);
        clientTCPReceiverThread.start();
    }
}
