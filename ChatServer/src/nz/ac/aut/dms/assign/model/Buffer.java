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
public class Buffer {
    private String message = null;
    private String username = null;
    private boolean bufferFull = false;

    public Buffer() {
    }

    /**
     * @return the message
     */
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
     * @return the bufferFull
     */
    public boolean isBufferFull() {
        return bufferFull;
    }

    /**
     * @param bufferFull the bufferFull to set
     */
    public void setBufferFull(boolean bufferFull) {
        this.bufferFull = bufferFull;
    }
    
    
}
