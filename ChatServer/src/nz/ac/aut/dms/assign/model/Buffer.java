/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author xfn
 */
class Buffer {

    private ArrayList<String> clients;
    private ArrayBlockingQueue<String> messages;

    public Buffer() {
        clients = new ArrayList<>();
        messages = new ArrayBlockingQueue<>(100, true);
    }

    String getConnectedClientNames() {
        return getClients().toString();

        //return s.substring(1, s.length() - 1);
    }

    void addClient(String message) {
        getClients().add(getOriginalClientName(message));
    }

    void removeClient(String message) {
        getClients().remove(getOriginalClientName(message));
    }

    boolean hasMoreMessage() {
        return !(messages.isEmpty());
    }

    ArrayList<String> getMessageByDestinationClientName(String destinationClientName) {
        ArrayList<String> result = new ArrayList<>();

        for (String message : getMessages()) {
            if (getDestinationClientName(message).equals(destinationClientName)) {
                result.add(message);
            }
        }

        return result;
    }

    void addMessage(String message) {
        getMessages().add(message);
    }

    void removeMessage(String message) {
        getMessages().remove(getMessageContent(message));
    }

    /**
     * @return the clients
     */
    public ArrayList<String> getClients() {
        return clients;
    }

    /**
     * @param clients the clients to set
     */
    public void setClients(ArrayList<String> clients) {
        this.clients = clients;
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

    String getHeadMessage() {
        return messages.peek();
    }

    void poll() {
        messages.poll();
    }

    public String getMessageContent(String message) {
        String[] strArray = message.trim().split("\\|");

        return strArray[0];
    }

    public String getOriginalClientName(String message) {
        String[] strArray = message.trim().split("\\|");

        return strArray[1];
    }

    public String getDestinationClientName(String message) {
        String[] strArray = message.trim().split("\\|");

        return strArray[2];
    }
}
