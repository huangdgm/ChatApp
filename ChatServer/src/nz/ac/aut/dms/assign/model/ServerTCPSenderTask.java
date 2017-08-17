package nz.ac.aut.dms.assign.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dong Huang
 */
public class ServerTCPSenderTask implements Runnable {

    ObjectOutputStream oos = null;
    Socket clientSocket = null;
    Buffer buffer = null;

    public ServerTCPSenderTask(Socket clientSocket, Buffer buffer) {
        this.clientSocket = clientSocket;
        this.buffer = buffer;

        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServerTCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        while (true) {
            if (!buffer.isBufferEmpty()) {
                for (FullMessage fullMessage : buffer.getFullMessages().values()) {
                    if (fullMessage.getMessage().getMessageType().equals("BROADCAST")) {
                        try {
                            oos.writeObject(fullMessage.getMessage());
                        } catch (IOException ex) {
                            Logger.getLogger(ServerTCPSenderTask.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        //buffer.getFullMessages().remove(fullMessage.getMessage().getToUser());
                    } else if (fullMessage.getMessage().getMessageType().equals("PRIVATE")) {
                        if (fullMessage.getClientSocket() == clientSocket) {
                            try {
                                oos.writeObject(fullMessage.getMessage());
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

}
