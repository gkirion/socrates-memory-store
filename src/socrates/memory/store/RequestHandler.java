/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socrates.memory.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author george
 */
public class RequestHandler implements Runnable {
    
    private Socket socket;
    private DataStore store;
    
    public RequestHandler(Socket socket, DataStore store) {
        this.socket = socket;
        this.store = store;
    }

    @Override
    public void run() {
        try {
            try {
                InputStream instream = socket.getInputStream();
                OutputStream outstream = socket.getOutputStream();
                Scanner in = new Scanner(instream);
                PrintWriter out = new PrintWriter(outstream);
                if (in.hasNext()) {
                    String command = in.next();
                    if (command.equals("get")) {
                        String key = in.next();
                        String value = store.get(key);
                        out.println(value);
                        out.flush();
                    }
                    else if (command.equals("put")) {
                        String key = in.next();
                        String value = in.next();
                        store.put(key, value);
                        out.println("stored value " + value + " with key " + key);
                        out.flush();
                    }
                }
            }
            finally {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
