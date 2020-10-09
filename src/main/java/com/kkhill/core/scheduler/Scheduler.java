package com.kkhill.core.scheduler;

import com.kkhill.common.convention.EventType;
import com.kkhill.core.Catcher;
import com.kkhill.core.event.Event;
import com.kkhill.core.thing.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    private final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    private Queue<Service> polledServices = new ConcurrentLinkedQueue<>();
    private Queue<Service> pushedServices = new ConcurrentLinkedQueue<>();

    private Scheduler() {}

    private static class Holder {
        private final static Scheduler instance = new Scheduler();
    }

    public static Scheduler getInstance() {
        return Holder.instance;
    }


    /**
     * run all scheduled task
     */
    public void start() {
        executor.scheduleAtFixedRate(this::beat, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * poll all services should be polled
     */
    public void pollAll() {

        for(Service service : polledServices) {
            executor.submit((Callable<Object>) service::invoke);
        }
    }

    /**
     * heart beat of system, to keep the program going and fire event
     */
    public void beat() {
        Event e = new Event(EventType.PLATFORM, "heartbeat", null);
        Catcher.getEventBus().fire(e);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        logger.info("heartbeat: {}", formatter.format(e.getTimestamp()));
    }

}
