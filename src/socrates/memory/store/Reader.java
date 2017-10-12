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
public class Reader implements Runnable {
    
    private DataStore store;
    private Telemetry telemetry;
    private int n;
    
    public Reader(DataStore store, int n, Telemetry telemetry) {
        this.store = store;
        this.telemetry = telemetry;
        this.n = n;
    }

    @Override
    public void run() {
        for (int i = 0; i < n; i++) {
            String key = "" + Math.random();
            //telemetry.request();
            store.get(key);
        }
    }
    
}
