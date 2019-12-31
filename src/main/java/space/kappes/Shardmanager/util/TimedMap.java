package space.kappes.Shardmanager.util;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class TimedMap<K, V> extends HashMap<K, V> {

    public void put(K key, V value, long time) {
        put(key, value);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                remove(key);
            }
        }, time);
    }

    public void put(K key, V value, long time, Consumer<V> timeout) {
        put(key, value);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(containsKey(key))
                    timeout.accept(value);
                remove(key);
            }
        }, time);
    }

    public void put(K key, V value, long time, Runnable timeout) {
        put(key, value);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(containsKey(key))
                    timeout.run();
                remove(key);
            }
        }, time);
    }
}

