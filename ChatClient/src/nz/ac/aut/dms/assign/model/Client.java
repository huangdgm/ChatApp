/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.net.Socket;
import java.util.ArrayList;
import nz.ac.aut.dms.assign.gui.ChatGUI;

/**
 *
 * @author Dong Huang
 */
public class Client {

    private ChatEventListener eventListener;

    public static ClientStatus clientStatus;

    private Socket socket;
    private Buffer buffer;

    private ArrayList<Socket> sockets;

//    //private ChatClientSocket chatClientTCPSocket = null;
//    private ChatEventListener chatEventListener;
//
//    public Client() {
//    }
//
//    public void startClient() {
////        // start tcp thread for sending unicast messages to the server
////        ClientTCPSenderTask clientTCPConnectionTask = new ClientTCPSenderTask(getChatClientTCPSocket());
////        Thread clientTCPSenderThread = new Thread(clientTCPConnectionTask);
////        clientTCPSenderThread.start();
////
////        // start tcp thread for receiving unicast messages from the server
////        ClientTCPReceiverTask clientTCPReceiverTask = new ClientTCPReceiverTask(getChatClientTCPSocket());
////        Thread clientTCPReceiverThread = new Thread(clientTCPReceiverTask);
////        clientTCPReceiverThread.start();
////
////        // start udp thread for receiving broadcast messages from the server
////        ClientMulticastReceiverTask clientMulticastReceiverTask = new ClientMulticastReceiverTask();
////        Thread clientMulticastReceiverThread = new Thread(clientMulticastReceiverTask);
////        clientMulticastReceiverThread.start();
//    }
//
////    /**
////     * @return the chatClientTCPSocket
////     */
////    public ChatClientSocket getChatClientTCPSocket() {
////        return chatClientTCPSocket;
////    }
////
////    /**
////     * @param chatClientTCPSocket the chatClientTCPSocket to set
////     */
////    public void setChatClientTCPSocket(ChatClientSocket chatClientTCPSocket) {
////        this.chatClientTCPSocket = chatClientTCPSocket;
////    }
//    /**
//     * @return the chatEventListener
//     */
//    public ChatEventListener getChatEventListener() {
//        return chatEventListener;
//    }
//
//    /**
//     * @param chatEventListener the chatEventListener to set
//     */
//    public void setChatEventListener(ChatEventListener chatEventListener) {
//        this.chatEventListener = chatEventListener;
//    }
//
//    private void notifyChatEventListener() {
//        if (chatEventListener != null) {
//            getChatEventListener().chatStateChanged();
//        }
//    }
    public void addChatEventListener(ChatEventListener eventListener) {
        this.eventListener = eventListener;
    }

    private void notifyGameEventListeners() {
        eventListener.stateChanged();
    }

    public String[] getConnectedClients() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getServerIP() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getServerPort() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getYourName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ClientStatus getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getGreetMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void resetChatWindow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getFarewellMessage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getChatHistory(String friend) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
