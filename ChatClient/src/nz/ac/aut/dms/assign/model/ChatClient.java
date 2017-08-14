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

    public static final String MULTICAST_ADDR = "224.0.0.4";
    public static final int MULTICAST_PORT = 8767;
    public static boolean stopClient = false;
    public static final int SERVER_PORT = 8765;

    //private static ChatClientSocket chatClientTCPSocket = null;
    //private static DatagramSocket UDPSocket = null;

    public static void startClient() {
        // start udp thread for receiving broadcast messages of current connected clients
        ClientUDPConnectionTask clientUDPConnectionTask = new ClientUDPConnectionTask();
        Thread clientUDPConnectionThread = new Thread(clientUDPConnectionTask);

        clientUDPConnectionThread.start();

        // start tcp thread for communicating with the server tcp thread
        ClientTCPConnectionTask clientTCPConnectionTask = new ClientTCPConnectionTask();
        Thread clientTCPConnectionThread = new Thread(clientTCPConnectionTask);
        
        clientTCPConnectionThread.start();
    }
}
