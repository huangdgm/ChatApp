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
    private String username = null;
    private InetAddress destinationInetAddress = null;

    public Message(String message, String userName, InetAddress destinationInetAddress) {
        this.message = message;
        this.username = userName;
        this.destinationInetAddress = destinationInetAddress;
    }

    public abstract String getMessageType();

    public String getMessage() {
        return message;
    }

    //public String getDate() {
    //    DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
    //    
    //    return dateFormatter.format(date);
    //}
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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
}
