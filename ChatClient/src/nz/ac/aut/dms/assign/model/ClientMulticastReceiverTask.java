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
import nz.ac.aut.dms.assign.gui.ChatGUI;

/**
 *
 * @author Dong Huang
 */
public class ClientMulticastReceiverTask implements Runnable {

    private ChatGUI chatGUI = null;

    public ClientMulticastReceiverTask(ChatGUI chatGUI) {
        this.chatGUI = chatGUI;
    }

    @Override
    public void run() {
        String previousUsers = null;

        while (!ChatClient.stopClient) {
            try {
                InetAddress multicastAddress = InetAddress.getByName(ChatClient.MULTICAST_ADDR);
                byte[] buf = new byte[256];
                MulticastSocket multicastSocket = new MulticastSocket(ChatClient.MULTICAST_PORT);
                multicastSocket.joinGroup(multicastAddress);
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                // The receive method blocks until receiving a datagram packet
                multicastSocket.receive(datagramPacket);
                String msg = new String(buf, 0, buf.length);

                // If there's update with the friendlist, then update the jList
                if (!msg.equals(previousUsers)) {
                    chatGUI.getjListFriendList().setListData(msg.substring(1, msg.indexOf("]")).split(", "));
                }

                previousUsers = msg;
            } catch (IOException e) {
                System.out.println("Client could not make connection: " + e.getMessage());
                ChatClient.stopClient = true;
            }
        }
    }
}
