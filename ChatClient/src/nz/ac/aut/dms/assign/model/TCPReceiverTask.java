/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Dong Huang
 */
public class TCPReceiverTask implements Runnable {

    private Socket socket = null;
    private DataInputStream inputStream = null;
    private Buffer buffer = null;

    public TCPReceiverTask(Socket socket, Buffer buffer) {
        this.socket = socket;
        this.buffer = buffer;

        try {
            inputStream = new DataInputStream(socket.getInputStream());
            System.out.println("Info: input stream successfully created.");
        } catch (IOException e) {
            System.out.println("Error: error creating the input stream. " + e.getMessage());
            Client.clientStatus = Status.OFFLINE;
        }
    }

    @Override
    public void run() {
        while (Client.clientStatus == Status.ONLINE) {
            String messageInput = null;

            try {
                if (inputStream.available() > 0) {
                    messageInput = inputStream.readUTF();
                    System.out.println("Info: successfully read messages from input stream : " + messageInput);
                }
            } catch (IOException ex) {
                System.out.println("Error: error in read from DataInputStream. " + ex.getMessage());
                Client.clientStatus = Status.OFFLINE;
            }

            if (messageInput != null) {
                buffer.addMessage(messageInput);
                System.out.println("Info: successfully add message to buffer.");
            }
        }

        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("Info: successfully close input stream and all the sockets.");
        } catch (IOException e) {
            System.out.println("Error: error closing the input stream or socket. " + e.getMessage());
            Client.clientStatus = Status.OFFLINE;
        }

        System.out.println("Info: client is offline. Closing connection with " + socket.getInetAddress() + ":" + socket.getPort());
    }
}
