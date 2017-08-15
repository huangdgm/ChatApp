/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ClientTCPSenderTask implements Runnable {

    private int i = (int) (Math.random() * 100);
    private ObjectOutputStream oos = null;
    private ChatClientSocket chatClientTCPSocket = null;

    public ClientTCPSenderTask(ChatClientSocket chatClientTCPSocket) {
        this.chatClientTCPSocket = chatClientTCPSocket;
        
        try {
            oos = new ObjectOutputStream(chatClientTCPSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientTCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Scanner keyboardInputScanner = new Scanner(System.in);

        try {
            HandshakeMessage handshakeMessage = new HandshakeMessage("this is handshake message", "dong-" + i, "SERVER", InetAddress.getLocalHost(), ChatClient.SERVER_PORT);
            oos.writeObject(handshakeMessage);
            // message = (Message) (ois.readObject());

            System.out.println("Connection made with " + chatClientTCPSocket.getInetAddress() + ":" + chatClientTCPSocket.getPort());

            System.out.println("Enter text or done to exit.");
            String clientRequest;

            do {
                clientRequest = keyboardInputScanner.nextLine();

                if (!clientRequest.equals("done")) {
                    PrivateMessage pm = new PrivateMessage(clientRequest, "dong-" + i, "TOUSER", InetAddress.getLocalHost(), ChatClient.SERVER_PORT);
                    oos.writeObject(pm);
                    //message = (Message) (ois.readObject());
                    //System.out.println("Server response: " + message.getMessage());
                } else {
                    DisconnectMessage dm = new DisconnectMessage(clientRequest, "dong-" + i, "SERVER", InetAddress.getLocalHost(), ChatClient.SERVER_PORT);
                    oos.writeObject(dm);
                }
            } while (!"done".equalsIgnoreCase(clientRequest.trim()));
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } finally {
            ChatClient.stopClient = true;
        }
    }

}
