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
import nz.ac.aut.dms.assign.gui.ChatGUI;

/**
 *
 * @author Dong Huang
 */
public class ClientTCPReceiverTask implements Runnable {

    Socket tcpSocket = null;
    ObjectInputStream ois = null;
    ChatGUI chatGUI = null;

    public ClientTCPReceiverTask(Socket tcpSocket, ChatGUI chatGUI) {
        this.tcpSocket = tcpSocket;
        this.chatGUI = chatGUI;

        try {
            ois = new ObjectInputStream(tcpSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        Message message = null;

        do {
            try {
                message = (Message) (ois.readObject());
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ClientTCPReceiverTask.class.getName()).log(Level.SEVERE, null, ex);
                ChatClient.stopClient = true;
            }

            // Update the chat history
            if (message != null) {
                chatGUI.getUsersAndChatHistory().put(message.getFromUser(), message.getFromUser() + " : " + message.getMessage() + "\n");
            }
        } while (!ChatClient.stopClient);

        try {
            if (ois != null) {
                ois.close();
            }
            if (tcpSocket != null) {
                tcpSocket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Closing connection with " + tcpSocket.getInetAddress() + ":" + tcpSocket.getPort());
    }

}
