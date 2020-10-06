package com.kkhill.core.event;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventBus {

    private Map<EventType, Queue<EventConsumer>> bus;

    private EventBus() {
        this.bus = new ConcurrentHashMap<>();
        EventType[] eventTypes =  EventType.values();
        for(EventType eventType : eventTypes) {
            this.bus.put(eventType, new ConcurrentLinkedQueue<>());
        }
    }

    private static class Holder {
        private static EventBus instance = new EventBus();
    }

    public static EventBus getInstance() {
        return Holder.instance;
    }

    public void listen(EventType type, EventConsumer consumer) {

        Queue<EventConsumer> consumers = this.bus.get(type);
        consumers.add(consumer);
    }

    public void fire(Event event) {

        Queue<EventConsumer> consumers = this.bus.get(event.getType());
        for(EventConsumer consumer : consumers) {
            consumer.execute();
        }
    }
}
