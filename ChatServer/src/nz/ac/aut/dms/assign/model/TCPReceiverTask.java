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
            System.out.println("Info: input stream successfully created.");
        } catch (IOException ex) {
            System.out.println("Error: error creating the input stream. " + ex.getMessage());
            Server.serverStatus = Status.OFFLINE;
        }
    }

    @Override
    public void run() {
        while (Server.serverStatus == Status.ONLINE) {
            String inputMessage = null;

            try {
                if (getInputStream().available() > 0) {
                    inputMessage = getInputStream().readUTF();
                    System.out.println("Info: successfully read messages from input stream : " + inputMessage);
                }
            } catch (IOException ex) {
                System.out.println("Error: error reading messages from the input stream. " + ex.getMessage());
                Server.serverStatus = Status.OFFLINE;
            }

            switch (getMessageType(inputMessage)) {
                case "connect":
                    getBuffer().addClient(inputMessage);
                    setRemoteClientName(inputMessage);
                    break;
                case "disconnect":
                    getBuffer().removeClient(inputMessage);
                    Server.serverStatus = Status.OFFLINE;
                    break;
                case "private":
                case "broadcast":
                    getBuffer().addMessage(inputMessage);
                    break;
                default:
                    break;
            }
        }

        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            System.out.println("Info: successfully close input stream and all the client sockets.");
        } catch (IOException e) {
            System.out.println("Error: error closing the input stream or socket. " + e.getMessage());
            Server.serverStatus = Status.OFFLINE;
        }

        System.out.println("Info: server is offline. Closing connection with " + clientSocket);
    }

    /**
     *
     * @param message
     * @return
     */
    private String getMessageType(String message) {
        if (message != null) {
            String[] strArray = message.trim().split("\\|");
            return strArray[3];
        } else {
            return null;
        }
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
        this.remoteClientName = message.trim().split("\\|")[2];
    }

    public String getRemoteClientName() {
        return remoteClientName;
    }
}
