/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socrates.memory.store;

/**
 *
 * @author george
 */
public class Writer implements Runnable {
    
    private DataStore store;
    private Telemetry telemetry;
    private int n;
    
    public Writer(DataStore store, int n, Telemetry telemetry) {
        this.store = store;
        this.telemetry = telemetry;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
            String key = "" + Math.random();
            String value = "" + Math.random();
            //telemetry.request();
            store.put(key, value);
        }
    }
    
}
