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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ClientTCPConnectionTask implements Runnable {

    @Override
    public void run() {
        Scanner keyboardInputScanner = new Scanner(System.in);
        ChatClientSocket chatClientTCPSocket = null;

        try {
            Message message = null;
            chatClientTCPSocket = new ChatClientSocket(InetAddress.getLocalHost(), ChatClient.SERVER_PORT);

            ObjectOutputStream oos = new ObjectOutputStream(chatClientTCPSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(chatClientTCPSocket.getInputStream());

            HandshakeMessage handshakeMessage = new HandshakeMessage("this is handshake message", "dong", InetAddress.getLocalHost());
            oos.writeObject(handshakeMessage);
            // message = (Message) (ois.readObject());

            System.out.println("Connection made with " + chatClientTCPSocket.getInetAddress() + ":" + chatClientTCPSocket.getPort());

            System.out.println("Enter text or done to exit.");
            String clientRequest;

            do {
                clientRequest = keyboardInputScanner.nextLine();

                PrivateMessage pm = new PrivateMessage(clientRequest, "dong", InetAddress.getLocalHost());
                oos.writeObject(pm);

                message = (Message) (ois.readObject());
                System.out.println("Server response: " + message.getMessage());
            } while (!"done".equalsIgnoreCase(clientRequest.trim()));
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ChatClient.stopClient = true;
        }
    }

}
