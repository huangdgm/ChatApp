/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Administrator
 */
public class ChatServerSocket extends ServerSocket {

    //private final int serverPort;

    public ChatServerSocket(int serverPort) throws IOException {
        super(serverPort);
    }

}
