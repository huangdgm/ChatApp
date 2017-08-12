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

/**
 *
 * @author Dong Huang
 */
class Connection extends Thread {

    final String DONE = "done";
    DataInputStream in = null;
    DataOutputStream out = null;
    Socket clientSocket = null;

    /**
     *
     * @param clientSocket
     */
    public Connection(Socket clientSocket) {
        try {
            this.clientSocket = clientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            //this.start();
        } catch (IOException e) {
            System.out.println("Connection: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String clientRequest;

            do {
                clientRequest = in.readUTF();
                System.out.println("Received in Connection Thread line: " + clientRequest);

                String serverResponse = "Hello " + clientRequest;
                out.writeUTF(serverResponse);
            } while (clientRequest != null && !DONE.equalsIgnoreCase(clientRequest.trim()));

            System.out.println("Closing connection with " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
