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
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("Info: output stream successfully created.");
        } catch (IOException ex) {
            System.out.println("Error: error creating output stream. " + ex.getMessage());
            Server.serverStatus = Status.OFFLINE;
        }
    }

    @Override
    public void run() {
        while (Server.serverStatus == Status.ONLINE) {
            if (buffer.hasMoreMessage() && remoteClientName != null) {
                String message = buffer.getHeadMessage();

                if (buffer.getDestinationClientName(message).equals(remoteClientName)) {
                    try {
                        outputStream.writeUTF(message);
                        System.out.println("Info: successfully write message to output stream.");
                    } catch (IOException ex) {
                        System.out.println("Error: error writing message to output stream. " + ex.getMessage());
                    }

                    buffer.poll();
                    System.out.println("Info: successfully poll off the head message from the queue.");
                }
            }
        }

        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            System.out.println("Info: successfully close output stream and all the client sockets.");
        } catch (IOException e) {
            System.out.println("Error: error closing the output stream or socket. " + e.getMessage());
            Server.serverStatus = Status.OFFLINE;
        }

        System.out.println("Info: server is offline. Closing connection with " + clientSocket);
        Server.serverStatus = Status.OFFLINE;
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
