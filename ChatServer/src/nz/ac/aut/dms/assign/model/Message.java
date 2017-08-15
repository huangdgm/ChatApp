/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.Serializable;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Dong Huang
 */
public abstract class Message implements Serializable {

    private String message = null;
    private String fromUser = null;
    private String toUser = null;
    private InetAddress destinationInetAddress = null;
    private int destinationPort = 0;

    public Message(String message, String fromUser, String toUser, InetAddress destinationInetAddress, int destinationPort) {
        this.message = message;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.destinationInetAddress = destinationInetAddress;
        this.destinationPort = destinationPort;
    }

    public abstract String getMessageType();

    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the fromUser
     */
    public String getFromUser() {
        return fromUser;
    }

    /**
     * @param fromUser the fromUser to set
     */
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * @return the toUser
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * @param toUser the toUser to set
     */
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    /**
     * @return the destinationInetAddress
     */
    public InetAddress getDestinationInetAddress() {
        return destinationInetAddress;
    }

    /**
     * @param destinationInetAddress the destinationInetAddress to set
     */
    public void setDestinationInetAddress(InetAddress destinationInetAddress) {
        this.destinationInetAddress = destinationInetAddress;
    }

    /**
     * @return the destinationPort
     */
    public int getDestinationPort() {
        return destinationPort;
    }

    /**
     * @param destinationPort the destinationPort to set
     */
    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    
}
