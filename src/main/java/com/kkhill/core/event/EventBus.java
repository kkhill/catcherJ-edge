package com.kkhill.core.event;

import com.kkhill.core.Catcher;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventBus {

    private Map<String, Queue<EventConsumer>> bus;

    private EventBus() {
        this.bus = new ConcurrentHashMap<>();
    }

    private static class Holder {
        private static EventBus instance = new EventBus();
    }

    public static EventBus getInstance() {
        return Holder.instance;
    }

    synchronized public void listen(String type, EventConsumer consumer) {

        Queue<EventConsumer> consumers = this.bus.get(type);
        if(consumers == null) {
            consumers = new ConcurrentLinkedQueue<>();
        }
        consumers.add(consumer);
    }

    public void fire(Event event) {

        Queue<EventConsumer> consumers = this.bus.get(event.getType());
        for(EventConsumer consumer : consumers) {
            Catcher.getExecutor().submit(() -> consumer.handle(event));
        }
    }
}
