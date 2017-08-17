/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.util.ArrayList;

/**
 *
 * @author xfn
 */
class Buffer {

    private ArrayList<String> clients;
    private ArrayList<String> messages;

    public Buffer() {
        clients = new ArrayList<>();
        messages = new ArrayList<>();
    }

    String getConnectedClientNames() {
        return clients.toString();
    }

    void addClient(String message) {
        clients.add(getOriginalClientName(message));
    }

    void removeClient(String message) {
        clients.remove(getOriginalClientName(message));
    }

    boolean hasMoreMessage() {
        return !(messages.isEmpty());
    }

    ArrayList<String> getMessageByDestinationClientName(String destinationClientName) {
        ArrayList<String> result = new ArrayList<>();

        for (String message : messages) {
            if (getDestinationClientName(message).equals(destinationClientName)) {
                result.add(message);
            }
        }

        return result;
    }

    void addMessage(String message) {
        messages.add(getMessageContent(message));
    }

    void removeMessage(String message) {
        messages.remove(getMessageContent(message));
    }

    private String getMessageContent(String message) {
        String[] strArray = message.trim().split("|");

        return strArray[0];
    }

    private String getOriginalClientName(String message) {
        String[] strArray = message.trim().split("|");

        return strArray[1];
    }

    private String getDestinationClientName(String message) {
        String[] strArray = message.trim().split("|");

        return strArray[2];
    }
}
