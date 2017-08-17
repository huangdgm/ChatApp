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
public class PrivateMessage extends Message {

    public PrivateMessage(String message, String fromUser, String toUser) {
        super(message, fromUser, toUser);
    }

    @Override
    public String getMessageType() {
        return "PRIVATE";
    }

}
