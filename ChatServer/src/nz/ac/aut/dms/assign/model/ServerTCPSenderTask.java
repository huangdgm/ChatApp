package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class ServerTCPSenderTask implements Runnable {

    ObjectOutputStream oos = null;
    ArrayList<Message> messages = null;
    ArrayList<User> users = null;
    Socket clientSocket = null;

    public ServerTCPSenderTask(Socket clientSocket, ArrayList<User> users, ArrayList<Message> messages) {
        this.messages = messages;
        this.users = users;
        this.clientSocket = clientSocket;

        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerTCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        while (true) {
            if (!messages.isEmpty()) {
                for (Message message : messages) {
                    // String toUser = message.getToUser();
                    
                    if (message.getMessageType().equals("BROADCAST")) {
                        try {
                            oos.writeObject(message.getMessage());
                        } catch (IOException ex) {
                            Logger.getLogger(ServerTCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        //buffer.getFullMessages().remove(fullMessage.getMessage().getToUser());
                    } else if (message.getMessageType().equals("PRIVATE")) {
                        try {
                            oos.writeObject(message.getMessage());
                        } catch (IOException ex) {
                            Logger.getLogger(ServerTCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        //buffer.getFullMessages().remove(fullMessage.getMessage().getToUser());
                    }
                }
            }
        }
    }
}
