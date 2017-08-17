/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ServerMulticastSenderTask implements Runnable {

    private DatagramSocket datagramSocket = null;
    private ArrayList<User> users = null;

    public ServerMulticastSenderTask(ArrayList<User> users) {
        this.users = users;
        
        try {
            datagramSocket = new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(ServerMulticastSenderTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (!ChatServer.stopServer) {
            try {
                InetAddress multicastAddress = InetAddress.getByName(ChatServer.MULTICAST_ADDR);
                Message message = new BroadcastMessage(users.toString(), "SERVER", "ALL");
                System.out.println("Connected clients: " + users.toString());
                DatagramPacket broadcastDatagramPacket = new DatagramPacket(message.getMessage().getBytes(), message.getMessage().getBytes().length, multicastAddress, ChatServer.MULTICAST_PORT);
                
                datagramSocket.send(broadcastDatagramPacket);
                Thread.sleep(2000); // broadcast every 2 seconds
            } catch (IOException e) {
                System.out.println(e.getMessage());
                ChatServer.stopServer = true;
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                ChatServer.stopServer = true;
            }
        }
    }

}
