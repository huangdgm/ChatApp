/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class ServerTCPSenderTask implements Runnable {

    ObjectOutputStream oos = null;
    Socket clientSocket = null;
    Buffer buffer = null;

    public ServerTCPSenderTask(Socket clientSocket, Buffer buffer) {
        this.clientSocket = clientSocket;
        this.buffer = buffer;

        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerTCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (buffer.isBufferEmpty() == false) {
            for (Message message : buffer.getMessages().values()) {
                if (message.getDestinationInetAddress() == clientSocket.getInetAddress() && message.getDestinationPort() == clientSocket.getPort()) {
                    try {
                        oos.writeObject(message);
                    } catch (IOException ex) {
                        Logger.getLogger(ServerTCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    buffer.getMessages().remove(message.getToUser());
                }
            }
        }
    }

}
