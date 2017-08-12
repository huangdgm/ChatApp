/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

/**
 *
 * @author Dong Huang
 */
public class ChatClient {

    private static ChatClientSocket chatClientSocket = null;
    private static final int SERVER_PORT = 8765;

    public static void startClient() {
        Scanner keyboardInputScanner = new Scanner(System.in);
        DataOutputStream output = null;
        DataInputStream input = null;

        try {
            chatClientSocket = new ChatClientSocket(InetAddress.getLocalHost(), SERVER_PORT);
        } catch (IOException e) {
            System.out.println("Client could not make connection: " + e.getMessage());
        }

        try {
            output = new DataOutputStream(chatClientSocket.getOutputStream());
            input = new DataInputStream(chatClientSocket.getInputStream());

            System.out.println("Enter text or done to exit.");

            String clientRequest;

            do {
                clientRequest = keyboardInputScanner.nextLine();
                output.writeUTF(clientRequest);

                String serverResponse = input.readUTF();
                System.out.println("Server response: " + serverResponse);
            } while (!"done".equalsIgnoreCase(clientRequest.trim()));
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
