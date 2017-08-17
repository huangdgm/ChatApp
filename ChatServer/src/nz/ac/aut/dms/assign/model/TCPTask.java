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
class TCPTask implements Runnable {

    private Socket clientSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String destinationClientName;
    private Buffer buffer;

    public TCPTask(Socket clientSocket, Buffer buffer) {
        this.clientSocket = clientSocket;
        this.buffer = buffer;

        try {
            this.outputStream = new DataOutputStream(clientSocket.getOutputStream());
            this.inputStream = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(TCPTask.class.getName()).log(Level.SEVERE, null, ex);
            Server.serverStatus = ServerStatus.OFFLINE;
        }
    }

    @Override
    public void run() {
        while (Server.serverStatus == ServerStatus.ONLINE) {
            // read from input stream
            String inputMessage = null;

            try {
                inputMessage = getInputStream().readUTF();
            } catch (IOException ex) {
                Logger.getLogger(TCPTask.class.getName()).log(Level.SEVERE, null, ex);
                Server.serverStatus = ServerStatus.OFFLINE;
            }

            System.out.println("Received from client: " + inputMessage);

            switch (getMessageType(inputMessage)) {
                case CONNECT:
                    buffer.addClient(inputMessage);
                    setDestinationClientName(inputMessage);
                    break;
                case DISCONNECT:
                    buffer.removeClient(inputMessage);
                    Server.serverStatus = ServerStatus.OFFLINE;
                    break;
                case PRIVATE:
                case BROADCAST:
                    buffer.addMessage(inputMessage);
                    break;
            }

            // write to output stream
            if (buffer.hasMoreMessage() && destinationClientName != null) {
                Iterator<String> i = buffer.getMessageByDestinationClientName(destinationClientName).iterator();

                while (i.hasNext()) {
                    String outputMessage = i.next();
                    try {
                        outputStream.writeUTF(outputMessage);
                        i.remove();
                    } catch (IOException ex) {
                        Logger.getLogger(TCPTask.class.getName()).log(Level.SEVERE, null, ex);
                        Server.serverStatus = ServerStatus.OFFLINE;
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
    private MessageType getMessageType(String message) {
        String[] strArray = message.trim().split("|");

        return MessageType.valueOf(strArray[3]);
    }

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
     * @return the inputStream
     */
    public DataInputStream getInputStream() {
        return inputStream;
    }

    /**
     * @param inputStream the inputStream to set
     */
    public void setInputStream(DataInputStream inputStream) {
        this.inputStream = inputStream;
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
        return destinationClientName;
    }

    /**
     * @param destinationClientName the destinationClientName to set
     */
    public void setDestinationClientName(String destinationClientName) {
        this.destinationClientName = destinationClientName;
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
