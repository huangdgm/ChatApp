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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ServerUDPConnectionTask implements Runnable {

    private DatagramSocket datagramSocket = null;
    private HashMap<String, User> users = null;
    private Buffer buffer = null;

    public ServerUDPConnectionTask(DatagramSocket datagramSocket, HashMap<String, User> users, Buffer buffer) {
        this.datagramSocket = datagramSocket;
        this.users = users;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (!ChatServer.stopServer) {
            try {
                InetAddress multicastAddress = InetAddress.getByName(ChatServer.MULTICAST_ADDR);
                datagramSocket = new DatagramSocket();

                Message message = new BroadcastMessage("connected users: " + users.toString(), "dong", multicastAddress);
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
