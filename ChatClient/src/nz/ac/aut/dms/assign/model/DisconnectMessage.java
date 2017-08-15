/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class DisconnectMessage extends Message {

    public DisconnectMessage(String message, String fromUser, String toUser,InetAddress destinationInetAddress, int destinationPort) {
        super(message, fromUser, toUser, destinationInetAddress, destinationPort);
    }

    @Override
    public String getMessageType() {
        return "DISCONNECT";
    }

}
