/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author Dong Huang
 */
public class MulticastTask implements Runnable {

    private Buffer buffer;

    public MulticastTask(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        String previousFriendListString = null;

        while (Client.clientStatus == Status.ONLINE) {
            try {
                InetAddress multicastAddress = InetAddress.getByName(ClientConfig.MULTICAST_ADDR);
                byte[] buf = new byte[256];
                MulticastSocket multicastSocket = new MulticastSocket(ClientConfig.MULTICAST_PORT);
                multicastSocket.joinGroup(multicastAddress);
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                multicastSocket.receive(datagramPacket);

                String currentFriendListString = new String(buf, 0, buf.length);
                currentFriendListString = currentFriendListString.substring(1, currentFriendListString.indexOf("]"));
                
                if (!(currentFriendListString.equals(previousFriendListString))) {
                    buffer.updateFriendList(currentFriendListString);
                    buffer.setFriendListNeedUpdated(true);
                }
                
                previousFriendListString = currentFriendListString;
                System.out.println("Info: receiving broadcast datagram packets: " + currentFriendListString);
            } catch (IOException e) {
                System.out.println("Error: could not receive broadcast datagram packets!" + e.getMessage());
                Client.clientStatus = Status.OFFLINE;
            }
        }
    }
}
