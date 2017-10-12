/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socrates.memory.store;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author george
 */
public class DataStore {
    
    private final HashMap<String, String> store;
    private final ReentrantReadWriteLock lock;
    private final AtomicInteger readers;
    private final AtomicInteger writers;
    private final AtomicInteger requests;
    private final AtomicInteger complaints;
    
    public DataStore() {
        store = new HashMap<>();
        lock = new ReentrantReadWriteLock();
        readers = new AtomicInteger();
        writers = new AtomicInteger();
        requests = new AtomicInteger();
        complaints = new AtomicInteger();
    }
    
    public String get(String key) {
        requests.incrementAndGet();
        int r = readers.incrementAndGet();
        int w = writers.get();
        if (w > 0) {
            //System.out.println("waiting for writers");
            complaints.incrementAndGet();
        }
        String value;
        //lock.readLock().lock();
        try {
            value = store.get(key);
        } finally {
          //  lock.readLock().unlock();
        }
        readers.decrementAndGet();
        return value;
    }
    
    public void put(String key, String value) {
        requests.incrementAndGet();
        int w = writers.incrementAndGet();
        int r = readers.get();
        if (w > 1) {
            //System.out.println("waiting for writers");
            complaints.incrementAndGet();
        }
        if (r > 1) {
            //System.out.println("waiting for readers");
            complaints.incrementAndGet();
        }
        lock.writeLock().lock();
        try {
            store.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
        writers.decrementAndGet();
    }
    
    public int getRequests() {
        return requests.get();
    }
    
    public int getComplaints() {
        return complaints.get();
    }
    
}
