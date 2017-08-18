/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.aut.dms.assign.gui.ChatGUI;

/**
 *
 * @author Dong Huang
 */
public class TCPReceiverTask implements Runnable {

    Socket clientSocket = null;
    ObjectInputStream ois = null;

    public TCPReceiverTask(Socket tcpSocket, ChatGUI chatGUI) {
        this.clientSocket = tcpSocket;
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
                Logger.getLogger(TCPReceiverTask.class.getName()).log(Level.SEVERE, null, ex);
                Client.stopClient = true;
            }

            if (message != null) {
                switch (message.getMessageType()) {
                    case "PRIVATE":
                    case "BROADCAST":
                        chatGUI.getUsersAndChatHistory().put(message.getFromUser(), message.getFromUser() + " : " + message.getMessage() + "\n");
                        break;
                    case "DISCONNECT":
                        // todo: reset GUI
                        break;
                }
            }
        } while (!Client.stopClient);

        try {
            if (ois != null) {
                ois.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Closing connection with " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
    }

}
