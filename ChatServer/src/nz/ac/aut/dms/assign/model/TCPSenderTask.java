/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
class TCPSenderTask implements Runnable {

    private Socket clientSocket;
    private DataOutputStream outputStream;
    private String remoteClientName;
    private Buffer buffer;

    public TCPSenderTask(Socket clientSocket, Buffer buffer, String remoteClientName) {
        this.clientSocket = clientSocket;
        this.buffer = buffer;
        this.remoteClientName = remoteClientName;

        try {
            this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(TCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
            Server.serverStatus = Status.OFFLINE;
        }
    }

    @Override
    public void run() {
        while (Server.serverStatus == Status.ONLINE) {
            if (buffer.hasMoreMessage() && remoteClientName != null) {
                Iterator<String> i = buffer.getMessageByDestinationClientName(remoteClientName).iterator();

                while (i.hasNext()) {
                    String outputMessage = i.next();
                    try {
                        outputStream.writeUTF(outputMessage);
                        i.remove();
                    } catch (IOException ex) {
                        Logger.getLogger(TCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
                        Server.serverStatus = Status.OFFLINE;
                    }
                }
            }
        }
    }

// 4 types of messages: (The message is initiated by the left participant)
// client - client
// how are you?|ning|dong|private                   private
// client - server
// connect|dong|server|connect                      connect
// disconnect|dong|server|disconnect                disconnect
// client - all clients         
// thank you all|ning|all|broadcast                 broadcast
// server - all clients
// disconnect|server|all|server_disconnect          to be added on the client side
    /**
     * @return the clientSocket
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * @param clientSocket the clientSocket to set
     */
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * @return the outputStream
     */
    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * @param outputStream the outputStream to set
     */
    public void setOutputStream(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * @return the destinationClientName
     */
    public String getDestinationClientName() {
        return remoteClientName;
    }

    /**
     * @param destinationClientName the destinationClientName to set
     */
    public void setDestinationClientName(String destinationClientName) {
        this.remoteClientName = destinationClientName;
    }

    /**
     * @return the buffer
     */
    public Buffer getBuffer() {
        return buffer;
    }

    /**
     * @param buffer the buffer to set
     */
    public void setBuffer(Buffer buffer) {
        this.buffer = buffer;
    }

}
