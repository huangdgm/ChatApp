/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

/**
 *
 * @author Dong Huang
 */
public class ChatClient {

    public static final String MULTICAST_ADDR = "224.0.0.4";
    public static final int MULTICAST_PORT = 8767;
    public static final int SERVER_PORT = 8765;
    public static boolean stopClient = false;

    //private ChatClientSocket chatClientTCPSocket = null;
    private ChatEventListener chatEventListener;

    public ChatClient() {
    }

    public void startClient() {
//        // start tcp thread for sending unicast messages to the server
//        ClientTCPSenderTask clientTCPConnectionTask = new ClientTCPSenderTask(getChatClientTCPSocket());
//        Thread clientTCPSenderThread = new Thread(clientTCPConnectionTask);
//        clientTCPSenderThread.start();
//
//        // start tcp thread for receiving unicast messages from the server
//        ClientTCPReceiverTask clientTCPReceiverTask = new ClientTCPReceiverTask(getChatClientTCPSocket());
//        Thread clientTCPReceiverThread = new Thread(clientTCPReceiverTask);
//        clientTCPReceiverThread.start();
//
//        // start udp thread for receiving broadcast messages from the server
//        ClientMulticastReceiverTask clientMulticastReceiverTask = new ClientMulticastReceiverTask();
//        Thread clientMulticastReceiverThread = new Thread(clientMulticastReceiverTask);
//        clientMulticastReceiverThread.start();
    }

//    /**
//     * @return the chatClientTCPSocket
//     */
//    public ChatClientSocket getChatClientTCPSocket() {
//        return chatClientTCPSocket;
//    }
//
//    /**
//     * @param chatClientTCPSocket the chatClientTCPSocket to set
//     */
//    public void setChatClientTCPSocket(ChatClientSocket chatClientTCPSocket) {
//        this.chatClientTCPSocket = chatClientTCPSocket;
//    }
    /**
     * @return the chatEventListener
     */
    public ChatEventListener getChatEventListener() {
        return chatEventListener;
    }

    /**
     * @param chatEventListener the chatEventListener to set
     */
    public void setChatEventListener(ChatEventListener chatEventListener) {
        this.chatEventListener = chatEventListener;
    }

    private void notifyChatEventListener() {
        if (chatEventListener != null) {
            getChatEventListener().chatStateChanged();
        }
    }
}
