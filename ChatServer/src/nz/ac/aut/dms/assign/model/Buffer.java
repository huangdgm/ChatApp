/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.util.HashMap;

/**
 *
 * @author Dong Huang
 */
public class Buffer {

    private HashMap<String, Message> messages = null;
    private boolean bufferEmpty = true;

    public Buffer() {
        messages = new HashMap<String, Message>();
    }

    /**
     * @return the messages
     */
    public HashMap<String, Message> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(HashMap<String, Message> messages) {
        this.messages = messages;
    }

    /**
     * @return the bufferEmpty
     */
    public boolean isBufferEmpty() {
        return bufferEmpty;
    }

    /**
     * @param bufferEmpty the bufferEmpty to set
     */
    public void setBufferEmpty(boolean bufferEmpty) {
        this.bufferEmpty = bufferEmpty;
    }

}
