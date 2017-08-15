/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
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
    User user = null;
    String threadName = null;
    HashMap<String, User> users = null;
    Buffer buffer = null;

    /**
     *
     * @param clientSocket
     */
    public ServerTCPReceiverTask(Socket clientSocket, HashMap<String, User> users, Buffer buffer) {
        this.clientSocket = clientSocket;
        this.users = users;
        this.buffer = buffer;

        user = new User();

        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        Message message = null;
        String messageType = null;
        // After the tcp connection is made, the server read the user information from the HandshakeMessage

        do {
            try {
                message = (Message) (ois.readObject());
                System.out.println("Received in Connection Thread line: " + message.getMessage());

                // Todo: write statements to process incoming messages, according to the message type
                messageType = message.getMessageType();

                switch (messageType) {
                    case "DISCONNECT":
                        users.remove(message.getFromUser());
                        break;
                    case "PRIVATE":
                    case "BROADCAST":
                        buffer.getMessages().put(message.getToUser(), message);
                        break;
                    case "HANDSHAKE":
                        setUser(clientSocket, message);
                        users.put(message.getFromUser(), user);
                        System.out.println("Connection made with " + user.getUsername() + user.getInetAddress() + ":" + user.getPort());
                        break;
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(ServerTCPReceiverTask.class.getName()).log(Level.SEVERE, null, ex);
                ChatServer.stopServer = true;
            }
        } while (!ChatServer.stopServer && message.getMessage() != null && !DONE.equalsIgnoreCase(message.getMessage().trim()));

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

    private void setUser(Socket clientSocket, Message message) {
        user.setInetAddress(clientSocket.getInetAddress());
        user.setUsername(message.getFromUser());
        user.setOnline(true);
        user.setPort(clientSocket.getPort());
    }
}
