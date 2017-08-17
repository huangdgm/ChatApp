/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

/**
 *
 * @author xfn
 */
public class Message {

    private String messageContent;
    private String originalClientName;
    private String destinationClientName;
    private MessageType messageType;

    public Message(String messageContent, String originalClient, String destinationClient, MessageType messageType) {
        this.messageContent = messageContent;
        this.originalClientName = originalClient;
        this.destinationClientName = destinationClient;
        this.messageType = messageType;
    }

    /**
     * @return the messageContent
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * @param messageContent the messageContent to set
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * @return the originalClientName
     */
    public String getOriginalClient() {
        return originalClientName;
    }

    /**
     * @param originalClient the originalClientName to set
     */
    public void setOriginalClient(String originalClient) {
        this.originalClientName = originalClient;
    }

    /**
     * @return the destinationClientName
     */
    public String getDestinationClient() {
        return destinationClientName;
    }

    /**
     * @param destinationClient the destinationClientName to set
     */
    public void setDestinationClient(String destinationClient) {
        this.destinationClientName = destinationClient;
    }

    /**
     * @return the messageType
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * @param messageType the messageType to set
     */
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

}
