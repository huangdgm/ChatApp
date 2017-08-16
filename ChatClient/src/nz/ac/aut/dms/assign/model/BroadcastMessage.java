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
public class BroadcastMessage extends Message {

    public BroadcastMessage(String message, String fromUser, String toUser) {
        super(message, fromUser, toUser);
    }

    @Override
    public String getMessageType() {
        return "BROADCAST";
    }

}
