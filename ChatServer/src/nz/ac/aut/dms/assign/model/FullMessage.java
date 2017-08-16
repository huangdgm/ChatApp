/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.net.InetAddress;

/**
 *
 * @author Administrator
 */
public class FullMessage {
    
    private Message message = null;
    private InetAddress inetAddress = null;
    private int port = 0;

    public FullMessage(Message message) {
        this.message = message;
    }

    /**
     * @return the message
     */
    public Message getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(Message message) {
        this.message = message;
    }

    /**
     * @return the inetAddress
     */
    public InetAddress getInetAddress() {
        return inetAddress;
    }

    /**
     * @param inetAddress the inetAddress to set
     */
    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }



}
