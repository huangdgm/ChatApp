/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
class ServerTCPReceiverTask implements Runnable {

    final String DONE = "done";
    ObjectInputStream ois = null;
    Socket clientSocket = null;
    String threadName = null;
    ArrayList<User> users = null;
    ArrayList<Message> messages = null;

    /**
     *
     * @param clientSocket
     */
    public ServerTCPReceiverTask(Socket clientSocket, ArrayList<User> users, ArrayList<Message> messages) {
        this.clientSocket = clientSocket;
        this.users = users;
        this.messages = messages;

        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
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

                // Todo: write statements to process incoming messages, according to the message type
                switch (message.getMessageType()) {
                    case "DISCONNECT":
                        users.remove(message.getFromUser());
                        break;
                    case "PRIVATE":
                        messages.add((PrivateMessage) message);
                        break;
                    case "BROADCAST":
                        messages.add((BroadcastMessage) message);
                        break;
                    case "HANDSHAKE":
                        //setUser(clientSocket, message);
                        User user = new User(message.getFromUser(), clientSocket);
                        users.add(user);
                        System.out.println("Connection made with: " + user.getUsername() + " / " + user.getClientSocket());
                        break;
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerTCPReceiverTask.class.getName()).log(Level.SEVERE, null, ex);
                ChatServer.stopServer = true;
            }

        } while (!ChatServer.stopServer
                && message.getMessage()
                != null && !DONE.equalsIgnoreCase(message.getMessage().trim()));

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



