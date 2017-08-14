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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
class ChatServerThread extends Thread {

    final String DONE = "done";
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    Socket clientSocket = null;
    User user = null;

    /**
     *
     * @param clientSocket
     */
    public ChatServerThread(Socket clientSocket) {
        this.clientSocket = clientSocket;

        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        Message message;

        try {
            message = (Message) (ois.readObject());

            User user = new User();

            user.setInetAddress(clientSocket.getInetAddress());
            user.setUsername(message.getUserName());
            user.setOnline(true);
            user.setPort(clientSocket.getPort());

            System.out.println("Connection made with " + user.getUsername() + user.getInetAddress() + ":" + user.getPort());
            oos.writeObject(message);

            do {
                message = (Message) (ois.readObject());

                System.out.println("Received in Connection Thread line: " + message.getMessage());

                oos.writeObject(message);
            } while (message.getMessage() != null && !DONE.equalsIgnoreCase(message.getMessage().trim()));

            System.out.println("Closing connection with " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChatServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
