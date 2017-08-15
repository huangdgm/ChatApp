/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ClientTCPReceiverTask implements Runnable {

    ChatClientSocket chatClientTCPSocket = null;
    ObjectInputStream ois = null;

    public ClientTCPReceiverTask(ChatClientSocket chatClientTCPSocket) {
        this.chatClientTCPSocket = chatClientTCPSocket;

        try {
            ois = new ObjectInputStream(chatClientTCPSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        Message message = null;
        // After the tcp connection is made, the server read the user information from the HandshakeMessage

        do {
            try {
                message = (Message) (ois.readObject());
                System.out.println("Received in Connection Thread line: " + message.getMessage());
            } catch (IOException | ClassNotFoundException ex) {
                ChatClient.stopClient = true;
            }
        } while (!ChatClient.stopClient);

        try {
            if (ois != null) {
                ois.close();
            }
            if (chatClientTCPSocket != null) {
                chatClientTCPSocket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Closing connection with " + chatClientTCPSocket.getInetAddress() + ":" + chatClientTCPSocket.getPort());
    }

}
