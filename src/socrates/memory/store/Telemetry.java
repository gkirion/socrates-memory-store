/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socrates.memory.store;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author george
 */
public class Telemetry {
    
    private int requests;
    private final ReentrantLock lock;
    private AtomicInteger req;
    
    public Telemetry() {
        requests = 0;
        lock = new ReentrantLock();
        req = new AtomicInteger();
    }
    
    public void request() {
        /*lock.lock();
        try {
            requests++;
        } finally {
            lock.unlock();
        }*/
        req.addAndGet(1);
    }
    
    public int getRequests() {
        int req;
        lock.lock();
        try {
            req = requests;
        } finally {
            lock.unlock();
        }
        return req;
    }
    
}
