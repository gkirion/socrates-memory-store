/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socrates.memory.store;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author george
 */
public class SocratesMemoryStore {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DataStore store, store2;
        Telemetry telemetry, telemetry2;
        int n = 256000;
        store = new DataStore();
        store2 = new DataStore();
        telemetry = new Telemetry();
        telemetry2 = new Telemetry();
        int nproc = Runtime.getRuntime().availableProcessors();
        System.out.println("this system has " + nproc + " hardware threads");
        System.out.println("storing " + n + " keys");
        long start = System.currentTimeMillis();
        long tot = 0;
        long c = 0;
        for (int i = 0; i < n; i++) {
            long s = System.nanoTime();
            String key = "" + Math.random();
            String value = "" + Math.random();
            store.put(key, value);
            store2.put(key, value);
            c++;
            //store2.put(key, value);
            long e = System.nanoTime();
            tot += e - s;
            if (c == 10000) {
                System.out.println(tot / c);
                c = 0;
                tot = 0;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("storing of " + n + " keys complete");
        System.out.println("Elapsed time: " + (end - start) / 1000.0 + " s");
        System.out.println("Nano time: " + tot / 1000000000.0 + " s");
        Reader reader = new Reader(store, 10*n, telemetry);
        Writer writer = new Writer(store, n, telemetry);
        Reader reader2 = new Reader(store2, 10*n, telemetry2);
        Writer writer2 = new Writer(store2, n, telemetry2);
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 1; i++) {
            threads[i] = new Thread(reader);
        }
        threads[1] = new Thread(reader);
        threads[2] = new Thread(reader);
        threads[3] = new Thread(reader);
        //System.out.println("running 3 readers and 1 writer " + n + " keys each");
        start = System.currentTimeMillis();
        for (int i = 0; i < 4; i++) {
            threads[i].start();
        }
        for (int i = 0; i < 4; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(SocratesMemoryStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        end = System.currentTimeMillis();
        System.out.println("running complete");
        System.out.println("Elapsed time: " + (end - start) / 1000.0 + " s");
        System.out.println("Telemetry stats");
        System.out.println("Store one, number of requests: " + telemetry.getRequests());
        System.out.println("Store two, number of requests: " + telemetry2.getRequests());
        System.out.println("Store one, requests: " + store.getRequests() + "\tcomplaints: " + store.getComplaints());
        System.out.println("Store two, requests: " + store2.getRequests() + "\tcomplaints: " + store2.getComplaints());
        
        try {
            ServerSocket server = new ServerSocket(8888);
            System.out.println("waiting for connections");
            while (true) {
                Socket client = server.accept();
                System.out.println("new connection ip: " + client.getInetAddress().getHostAddress() + " port: " + client.getPort());
                RequestHandler requestHandler = new RequestHandler(client, store);
                Thread t = new Thread(requestHandler);
                t.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(SocratesMemoryStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
