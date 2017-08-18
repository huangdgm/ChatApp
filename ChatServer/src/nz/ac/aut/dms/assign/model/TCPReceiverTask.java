/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jvb4600
 */
public class TCPReceiverTask implements Runnable {

    private Socket clientSocket;
    private DataInputStream inputStream;
    private Buffer buffer;
    private String remoteClientName;

    public TCPReceiverTask(Socket clientSocket, Buffer buffer, String remoteClientName) {
        this.clientSocket = clientSocket;
        this.buffer = buffer;
        this.remoteClientName = remoteClientName;

        try {
            inputStream = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(TCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
            Server.serverStatus = Status.OFFLINE;
        }
    }

    @Override
    public void run() {
        while (Server.serverStatus == Status.ONLINE) {
            String inputMessage = null;

            try {
                inputMessage = getInputStream().readUTF();
            } catch (IOException ex) {
                Logger.getLogger(TCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
                Server.serverStatus = Status.OFFLINE;
            }

            System.out.println("Received from client: " + inputMessage);

            switch (getMessageType(inputMessage)) {
                case CONNECT:
                    getBuffer().addClient(inputMessage);
                    setRemoteClientName(inputMessage);
                    break;
                case DISCONNECT:
                    getBuffer().removeClient(inputMessage);
                    Server.serverStatus = Status.OFFLINE;
                    break;
                case PRIVATE:
                case BROADCAST:
                    getBuffer().addMessage(inputMessage);
                    break;
            }
        }
    }

    private MessageType getMessageType(String message) {
        String[] strArray = message.trim().split("|");

        return MessageType.valueOf(strArray[3]);
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

    private void setRemoteClientName(String message) {
        this.remoteClientName = message.trim().split("|")[2];
    }
}
