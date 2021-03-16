package com.kkhill.core.event;

import com.kkhill.core.Catcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventBus {

    private static final Logger logger = LoggerFactory.getLogger(EventBus.class);

    // listen operation could be called by multi-tasks, such as:
    // 1. plugin loaded. Plugins should be loaded asynchronously, and they listen events
    // 2. update task defined by drivers could listen and fire event dynamically
    private final Map<String, Queue<EventConsumer>> bus;

    private EventBus() {
        this.bus = new ConcurrentHashMap<>();
    }

    private static class Holder {
        private static EventBus instance = new EventBus();
    }

    public static EventBus getInstance() {
        return Holder.instance;
    }

    public void listen(String type, EventConsumer consumer) {
        this.bus.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        Queue<EventConsumer> consumers = this.bus.get(type);
        consumers.add(consumer);
    }

    // TODO: remove operation could content with listen, be careful

    public void fire(Event event) {

        Queue<EventConsumer> consumers = this.bus.get(event.getType());
        if (consumers == null) return;
        for(EventConsumer consumer : consumers) {
            Catcher.getScheduler().getExecutor().submit(() -> consumer.handle(event));
        }
        logger.debug("event fired: {}, {}", event.getType(), event.getTimestamp());
    }
}
