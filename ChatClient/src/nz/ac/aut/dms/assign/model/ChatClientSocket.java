/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Administrator
 */
public class ChatClientSocket extends Socket {

    //private InetAddress serverInetAddress = null;
    //private int serverPort = 0;
    public ChatClientSocket(InetAddress serverInetAddress, int serverPort) throws IOException {
        //this.serverInetAddress = serverInetAddress;
        //this.serverPort = serverPort;
        super(serverInetAddress, serverPort);
    }

}
