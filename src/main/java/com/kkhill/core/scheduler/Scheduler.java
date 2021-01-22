package com.kkhill.core.scheduler;

import com.kkhill.common.event.EventType;
import com.kkhill.core.Catcher;
import com.kkhill.core.event.Event;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.concurrent.*;

public class Scheduler {

    private final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    // TODO: do some reject policy
    private ScheduledThreadPoolExecutor executor;
    private ConcurrentLinkedQueue<Service> pollServices;
    private ConcurrentLinkedQueue<Service> pushServices;

    private Scheduler() {
        executor = new ScheduledThreadPoolExecutor(poolSize);
        pollServices = new ConcurrentLinkedQueue<>();
        pushServices = new ConcurrentLinkedQueue<>();
    }

    private static int poolSize = 10;
    private static int heartbeat = 5;

    private static class Holder {
        private final static Scheduler instance = new Scheduler();
    }

    public static void initialize(int poolSize, int heartbeat) {
        Scheduler.poolSize = poolSize;
        Scheduler.heartbeat = heartbeat;
    }

    public static Scheduler getInstance() {
        return Holder.instance;
    }

    public ScheduledThreadPoolExecutor getExecutor() {
        return Holder.instance.executor;
    }

    public void start() {

        // heartbeat
        executor.scheduleAtFixedRate(this::beat, 0, this.heartbeat, TimeUnit.SECONDS);
        // run all scheduled task
        for(Service service : pollServices) {
            if(!service.isPolled()) continue;
            executor.scheduleAtFixedRate(()-> {
                try {
                    Catcher.getThingMonitor().callServiceAndNotify(service.getThingId(), service.getName(), null);
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }, 0, service.getPollInternal(), TimeUnit.SECONDS);
        }
        logger.info("scheduler started");
    }

    public void stop() {
        executor.shutdown();
    }

    /**
     * heart beat of system, to keep the program going and fire event
     */
    public void beat() {

        Event e = new Event(EventType.PLATFORM, "heartbeat", null);
//        Catcher.getEventBus().fire(e);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        logger.debug("heartbeat: {}", formatter.format(e.getTimestamp()));
    }

    public void addPolledService(Service s) {
        this.pollServices.offer(s);
    }

    public boolean removePolledService(Service s) {
        return this.pollServices.remove(s);
    }

}
