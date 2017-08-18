/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.aut.dms.assign.main;

import nz.ac.aut.dms.assign.gui.ChatGUI;
import nz.ac.aut.dms.assign.model.Client;

/**
 *
 * @author Dong Huang
 */
public class Main {

    public static void main(String[] args) {
        Client client = new Client();
        ChatGUI gui = new ChatGUI(client);

        // make the GUI visible
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui.setVisible(true);
            }
        });
    }
}
