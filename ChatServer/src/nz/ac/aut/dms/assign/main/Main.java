/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.main;

import java.util.ArrayList;
import java.util.HashMap;
import nz.ac.aut.dms.assign.model.ChatServer;
import nz.ac.aut.dms.assign.model.User;
import nz.ac.aut.dms.assign.model.Buffer;

/**
 *
 * @author Dong Huang
 */
public class Main {

    public static void main(String[] args) {
        ArrayList<User> users = new ArrayList<>();
        Buffer buffer = new Buffer();

        ChatServer chatServer = new ChatServer(users, buffer);

        chatServer.startServer();
    }
}
