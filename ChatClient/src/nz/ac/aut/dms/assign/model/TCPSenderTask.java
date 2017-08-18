/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Dong Huang
 */
public class TCPSenderTask implements Runnable {

    private Socket socket = null;
    private DataOutputStream outputStream = null;
    private Buffer buffer = null;

    public TCPSenderTask(Socket socket, Buffer buffer) {
        this.socket = socket;
        this.buffer = buffer;

        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("Info: output stream successfully created.");
        } catch (IOException ex) {
            System.out.println("Error: error creating the output stream " + ex.getMessage());
            Client.clientStatus = Status.OFFLINE;
        }
    }

    @Override
    public void run() {
        // send connect message
        String connectMessage = "connect|" + buffer.getClientName() + "|server|connect";

        try {
            outputStream.writeUTF(connectMessage);
            System.out.println("Info: successfully write to the output stream.");
        } catch (IOException ex) {
            System.out.println("Error: error sending connect message" + ex.getMessage());
            Client.clientStatus = Status.OFFLINE;
        }

        while (Client.clientStatus == Status.ONLINE) {
            if (buffer.hasMoreMessage()) {
                try {
                    outputStream.writeUTF(buffer.poll());
                } catch (IOException ex) {
                    System.out.println("Error: error writing message to output stream. " + ex.getMessage());
                    Client.clientStatus = Status.OFFLINE;
                }
                System.out.println("Info: successfully write message to output stream.");
                System.out.println("Info: successfully poll off the head message from the queue.");
            }
        }

        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("Info: successfully close output stream and all the sockets.");
        } catch (IOException e) {
            System.out.println("Error: error closing the output stream or socket. " + e.getMessage());
            Client.clientStatus = Status.OFFLINE;
        }

        System.out.println("Info: client is offline. Closing connection with " + socket);
    }
}
