/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class Client {

    private ChatEventListener eventListener;

    public static Status clientStatus;

    private Socket socket;
    private Buffer buffer;

    public Client() {
        buffer = new Buffer();
        clientStatus = clientStatus.OFFLINE;
        eventListener = null;
    }

    public String getClientName() {
        return buffer.getClientName();
    }

    public void addChatEventListener(ChatEventListener eventListener) {
        this.eventListener = eventListener;
    }

    private void notifyGameEventListeners() {
        eventListener.stateChanged();
    }

    public String[] getConnectedClients() {
        String connectedClientsString = buffer.getChatHistory().keySet().toString();

        return connectedClientsString.substring(1, connectedClientsString.length() - 1).split(", ");
    }

    public String getGreetMessage() {
        return "Welcome to the chat app!";
    }

    public String getFarewellMessage() {
        return "Goodbye! See you.";
    }

    public String getChatHistory(String friend) {
        return buffer.getChatHistory().get(friend);
    }

    public void setChatHistory(String friendName, String messageInput) {
        buffer.getChatHistory().put(friendName, getChatHistory(friendName) + messageInput);

        notifyGameEventListeners();
    }

    public void setChatHistoryForAll(String messageInput) {
        for (String friendName : buffer.getChatHistory().keySet()) {
            buffer.getChatHistory().put(friendName, getChatHistory(friendName) + messageInput);
        }

        notifyGameEventListeners();
    }

    public Status getClientStatus() {
        return Client.clientStatus;
    }

    public boolean isFriendListNeedUpdated() {
        return buffer.isFriendListNeedUpdated();
    }

    public void setClientName(String clientName) {
        this.buffer.setClientName(clientName);
    }

    public void connectServer(String serverIP, String serverPort, String clientName) {
        clientStatus = Status.ONLINE;
        
        MulticastTask multicastTask = new MulticastTask(buffer);
        Thread multicastThread = new Thread(multicastTask);
        multicastThread.start();

        try {
            socket = new Socket(InetAddress.getByName(ClientConfig.SERVER_ADDR), ClientConfig.SERVER_PORT);
            System.out.println("Info: socket successfully created.");
        } catch (UnknownHostException ex) {
            System.out.println("Error: error creating socket. " + ex.getMessage());
            clientStatus = Status.OFFLINE;
        } catch (IOException ex) {
            System.out.println("Error: error creating socket. " + ex.getMessage());
            clientStatus = Status.OFFLINE;
        }

        TCPSenderTask tcpSenderTask = new TCPSenderTask(socket, buffer);
        Thread tcpSenderThread = new Thread(tcpSenderTask);
        tcpSenderThread.start();

        TCPReceiverTask tcpReceiverTask = new TCPReceiverTask(socket, buffer);
        Thread tcpReceivThread = new Thread(tcpReceiverTask);
        tcpReceivThread.start();
        
        notifyGameEventListeners();
    }

    public void disconnectServer() {
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error: error closing socket. " + ex.getMessage());
        }

        clientStatus = clientStatus.OFFLINE;
        notifyGameEventListeners();
        System.out.println("Info: client closed with: " + socket);
    }
}
