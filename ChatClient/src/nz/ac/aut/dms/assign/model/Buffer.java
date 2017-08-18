/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Administrator
 */
public class Buffer {
    private ArrayBlockingQueue<String> messages;
    private ConcurrentHashMap<String, String> chatHistory;
    private String clientName;
    private boolean friendListNeedUpdated;
    
    public Buffer() {
        messages = new ArrayBlockingQueue<>(10, true);
        chatHistory = new ConcurrentHashMap(100);
        clientName = null;
        friendListNeedUpdated = false;
    }

    void addMessage(String message) {
        messages.add(message);
    }

    void updateFriendList(String friendListString) {
        String[] friendListArray = friendListString.split(", ");
        
        for(int index = 0; index < friendListArray.length; index++) {
            if(!(chatHistory.containsKey(friendListArray[index]))) {
                chatHistory.put(friendListArray[index], new String());
            }
        }
    }

    boolean hasMoreMessage() {
        return !(messages.isEmpty());
    }

    String getHeadMessage() {
        return messages.peek();
    }

    String poll() {
        return messages.poll();
    }

    /**
     * @return the messages
     */
    public ArrayBlockingQueue<String> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(ArrayBlockingQueue<String> messages) {
        this.messages = messages;
    }

    /**
     * @return the chatHistory
     */
    public ConcurrentHashMap<String, String> getChatHistory() {
        return chatHistory;
    }

    /**
     * @param chatHistory the chatHistory to set
     */
    public void setChatHistory(ConcurrentHashMap<String, String> chatHistory) {
        this.chatHistory = chatHistory;
    }

    String getClientName() {
        return clientName;
    }

    void setFriendListNeedUpdated(boolean b) {
        this.friendListNeedUpdated = b;
    }

    /**
     * @return the friendListNeedUpdated
     */
    public boolean isFriendListNeedUpdated() {
        return friendListNeedUpdated;
    }

    void setClientName(String clientName) {
        this.clientName = clientName;
    }
    
}
