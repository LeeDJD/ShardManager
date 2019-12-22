package space.kappes.Shardmanager.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventManager {

    private final List<ListenerAdapter> listenerAdapterList;

    public EventManager() {
        this.listenerAdapterList = new ArrayList<>();
    }

    public void addListener(ListenerAdapter listenerAdapter) {
        listenerAdapterList.add(listenerAdapter);
    }

    public void addListeners(ListenerAdapter... listenerAdapters) {
        listenerAdapterList.addAll(Arrays.asList(listenerAdapters));
    }

    public void call(Event event) {
        listenerAdapterList.forEach(listenerAdapter -> new Thread(() -> listenerAdapter.onEvent(event), "EventExecutor").start());
    }


}
