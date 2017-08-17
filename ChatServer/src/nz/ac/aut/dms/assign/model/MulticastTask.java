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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class MulticastTask implements Runnable {

    private DatagramSocket datagramSocket;
    private Buffer buffer;

    /**
     *
     * @param buffer
     */
    public MulticastTask(Buffer buffer) {
        this.buffer = buffer;

        try {
            datagramSocket = new DatagramSocket();
            System.out.println("Info: DatagramSocket created.");
        } catch (SocketException ex) {
            Logger.getLogger(MulticastTask.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: DatagramSocket not successfully created");
        }
    }

    @Override
    public void run() {
        while (Server.serverStatus == ServerStatus.ONLINE) {
            try {
                InetAddress multicastAddress = InetAddress.getByName(ServerConfig.MULTICAST_ADDR);
                Message message = new Message(buffer.getConnectedClientNames(), "SERVER", "ALL", MessageType.BROADCAST);
                DatagramPacket broadcastDatagramPacket = new DatagramPacket(message.getMessageContent().getBytes(), message.getMessageContent().getBytes().length, multicastAddress, ServerConfig.MULTICAST_PORT);
                datagramSocket.send(broadcastDatagramPacket);
                Thread.sleep(2000); // broadcast every 2 seconds
            } catch (IOException e) {
                Server.serverStatus = ServerStatus.OFFLINE;
            } catch (InterruptedException ex) {
                Logger.getLogger(MulticastTask.class.getName()).log(Level.SEVERE, null, ex);
                Server.serverStatus = ServerStatus.OFFLINE;
            }

            System.out.println("Info: Connected clients: " + buffer.getConnectedClientNames());
            System.out.println("Info: Broadcast datagram packet sent.");
        }
    }
}
