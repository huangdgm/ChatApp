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

    private HashMap<String, FullMessage> fullMessages = null;
    private boolean bufferEmpty = true;

    public Buffer() {
        fullMessages = new HashMap<String, FullMessage>();
    }

    /**
     * @return the fullMessages
     */
    public HashMap<String, FullMessage> getFullMessages() {
        return fullMessages;
    }

    /**
     * @param fullMessages the fullMessages to set
     */
    public void setFullMessages(HashMap<String, FullMessage> fullMessages) {
        this.fullMessages = fullMessages;
    }

    /**
     * @return the bufferEmpty
     */
    public boolean isBufferEmpty() {
        return fullMessages.isEmpty();
    }

    /**
     * @param bufferEmpty the bufferEmpty to set
     */
    public void setBufferEmpty(boolean bufferEmpty) {
        this.bufferEmpty = bufferEmpty;
    }

    

}
