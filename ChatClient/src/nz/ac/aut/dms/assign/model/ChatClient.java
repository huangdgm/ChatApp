/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class ChatClient {

    private static ChatClientSocket chatClientSocket = null;
    private static final int SERVER_PORT = 8765;
    private static String username = "dong";

    public static void startClient() {
        Scanner keyboardInputScanner = new Scanner(System.in);
        //DataOutputStream output = null;
        //DataInputStream input = null;

        try {
            chatClientSocket = new ChatClientSocket(InetAddress.getLocalHost(), SERVER_PORT);
        } catch (IOException e) {
            System.out.println("Client could not make connection: " + e.getMessage());
        }

        try {
            //output = new DataOutputStream(chatClientSocket.getOutputStream());
            //input = new DataInputStream(chatClientSocket.getInputStream());
            
            ObjectOutputStream oos = new ObjectOutputStream(chatClientSocket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(chatClientSocket.getInputStream());

            System.out.println("Enter text or done to exit.");

            String clientRequest;

            do {
                clientRequest = keyboardInputScanner.nextLine();
                //output.writeUTF(clientRequest);
                
                PrivateMessage pm = new PrivateMessage(clientRequest, username, InetAddress.getLocalHost());
                oos.writeObject(pm);

                //String serverResponse = input.readUTF();
                
                Message obj = (Message)(ois.readObject());
                System.out.println("Server response: " + obj.getMessage());
            } while (!"done".equalsIgnoreCase(clientRequest.trim()));
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
