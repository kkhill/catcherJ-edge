package com.kkhill.core.event;

import com.kkhill.Bootstrap;
import com.kkhill.core.Catcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventBus {

    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

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
        this.bus.put(type, consumers);
    }

    public void fire(Event event) {

        Queue<EventConsumer> consumers = this.bus.get(event.getType());
        if (consumers == null) return;
        for(EventConsumer consumer : consumers) {
            Catcher.getScheduler().getExecutor().submit(() -> consumer.handle(event));
        }
        logger.debug("event fired: {}, {}", event.getType(), event.getTimestamp());
    }
}
