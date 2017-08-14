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
public class HandshakeMessage extends Message {

    public HandshakeMessage(String message, String userName, InetAddress destinationInetAddress) {
        super(message, userName, destinationInetAddress);
    }

    @Override
    public String getMessageType() {
        return "HANDSHAKE";
    }

}
