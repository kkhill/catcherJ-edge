package com.kkhill.core.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EventBus {

    private Map<EventType, List<EventConsumer>> bus;
    private ReentrantReadWriteLock lock;

    public EventBus() {

        this.bus = new ConcurrentHashMap<EventType, List<EventConsumer>>();
        this.bus.put(EventType.STATE_UPDATED, new CopyOnWriteArrayList<EventConsumer>());
        this.bus.put(EventType.PROPERTY_UPDATED, new CopyOnWriteArrayList<EventConsumer>());
        this.bus.put(EventType.SERVICE_UPDATED, new CopyOnWriteArrayList<EventConsumer>());
        this.bus.put(EventType.PLATFORM, new CopyOnWriteArrayList<EventConsumer>());
        this.bus.put(EventType.THING, new CopyOnWriteArrayList<EventConsumer>());
        this.bus.put(EventType.ADDON, new CopyOnWriteArrayList<EventConsumer>());
        this.bus.put(EventType.DRIVER, new CopyOnWriteArrayList<EventConsumer>());
        this.bus.put(EventType.OTHERS, new CopyOnWriteArrayList<EventConsumer>());
    }

    public void listen(EventType type, EventConsumer consumer) {

        List<EventConsumer> consumers = this.bus.get(type);
        consumers.add(consumer);
    }

    public void fire(Event event) {

        List<EventConsumer> consumers = this.bus.get(event.getType());
        for(EventConsumer consumer : consumers) {
            consumer.execute();
        }
    }
}
