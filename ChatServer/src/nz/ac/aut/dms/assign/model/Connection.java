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
class Connection extends Thread {

    final String DONE = "done";
    ObjectInputStream ois = null;
    ObjectOutputStream oos = null;
    Socket clientSocket = null;

    /**
     *
     * @param clientSocket
     */
    public Connection(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            //in = new DataInputStream(clientSocket.getInputStream());
            //out = new DataOutputStream(clientSocket.getOutputStream());
            //this.start();
            ois = new ObjectInputStream(clientSocket.getInputStream());
            oos = new ObjectOutputStream(clientSocket.getOutputStream());

        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            //String clientRequest;
            Message message;

            do {
                //clientRequest = in.readUTF();
                message = (Message) (ois.readObject());

                System.out.println("Received in Connection Thread line: " + message.getMessage());

                // String serverResponse = "Hello " + message.getMessage();
                oos.writeObject(message);
                // out.writeUTF(serverResponse);
            } while (message.getMessage() != null && !DONE.equalsIgnoreCase(message.getMessage().trim()));

            System.out.println("Closing connection with " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
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
