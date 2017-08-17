/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import nz.ac.aut.dms.assign.gui.ChatGUI;

/**
 *
 * @author Dong Huang
 */
public class ClientTCPSenderTask implements Runnable {

    private Socket tcpSocket = null;
    private ChatGUI chatGUI = null;
    
    private ObjectOutputStream oos = null;

    public ClientTCPSenderTask(Socket tcpSocket, ChatGUI chatGUI) {
        this.tcpSocket = tcpSocket;
        this.chatGUI = chatGUI;

        try {
            oos = new ObjectOutputStream(tcpSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientTCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        // After the connection is made with the server(implemented in the ChatGUI), the client send a handshake message to server. 
        HandshakeMessage handshakeMessage = new HandshakeMessage("handshake message", chatGUI.getjTextFieldYourName().getText(), "SERVER");

        try {
            oos.writeObject(handshakeMessage);
        } catch (IOException ex) {
            Logger.getLogger(ChatGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            do {
                // enable or disable the send button
                if (chatGUI.getjTextAreaMessageInput().getText() != null && !(chatGUI.getjListFriendList().isSelectionEmpty())) {
                    chatGUI.getjButtonSend().setEnabled(true);
                } else {
                    chatGUI.getjButtonSend().setEnabled(false);
                }

                if (chatGUI.isSendButtonPressed()) {
                    String input = chatGUI.getjTextAreaMessageInput().getText();
                    String toUser = chatGUI.getjListFriendList().getSelectedValue();
                    String fromUser = chatGUI.getjTextFieldYourName().getText();

                    if (chatGUI.getjCheckBoxBroadcast().isSelected()) {
                        toUser = "ALL";
                        BroadcastMessage bm = new BroadcastMessage(input, fromUser, toUser);
                        oos.writeObject(bm);

                        // Update the GUI
                        chatGUI.getjTextAreaChatHistory().append(bm.getFromUser() + " : " + bm.getMessage() + "\n");
                        chatGUI.getjTextAreaMessageInput().setText("");
                        chatGUI.getjButtonSend().setEnabled(false);

                        chatGUI.getUsersAndChatHistory().put(bm.getFromUser(), bm.getFromUser() + " : " + bm.getMessage() + "\n");
                        chatGUI.setSendButtonPressed(false);
                    } else {
                        PrivateMessage pm = new PrivateMessage(input, fromUser, toUser);
                        oos.writeObject(pm);

                        // Update the GUI
                        chatGUI.getjTextAreaChatHistory().append(pm.getFromUser() + " : " + pm.getMessage() + "\n");
                        chatGUI.getjTextAreaMessageInput().setText("");
                        chatGUI.getjButtonSend().setEnabled(false);

                        chatGUI.getUsersAndChatHistory().put(pm.getFromUser(), pm.getFromUser() + " : " + pm.getMessage() + "\n");
                        chatGUI.setSendButtonPressed(false);
                    }
                }
            } while (true);
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } finally {
            ChatClient.stopClient = true;
        }

        try {
            if (oos != null) {
                oos.close();
            }
            if (tcpSocket != null) {
                tcpSocket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
