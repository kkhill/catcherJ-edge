package com.kkhill.core.scheduler;

import com.kkhill.common.convention.EventType;
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
    private static int heartbeat = 2;

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

    public static ExecutorService getServiceExecutor() {
        return Holder.instance.executor;
    }


    /**
     * run all scheduled task
     */
    public void start() {
        executor.scheduleAtFixedRate(this::beat, 0, this.heartbeat, TimeUnit.SECONDS);

        // poll service
        for(Service service : pollServices) {
            if(!service.isPolled()) continue;
            executor.scheduleAtFixedRate(()-> {
                        try {
                            Catcher.getThingMonitor().callService(service.getThingId(), service.getName());
                        } catch (NotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    , 0, service.getInternal(), TimeUnit.SECONDS);
            executor.submit(()->Catcher.getThingMonitor().callService(service.getThingId(), service.getName()));
        }
    }

    /**
     * heart beat of system, to keep the program going and fire event
     */
    public void beat() {

        Event e = new Event(EventType.PLATFORM, "heartbeat", null);
        Catcher.getEventBus().fire(e);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        logger.info("heartbeat: {}", formatter.format(e.getTimestamp()));
    }

    public void addPolledService(Service s) {
        this.pollServices.offer(s);
    }

    public boolean removePolledService(Service s) {
        return this.pollServices.remove(s);
    }


    /**
     * some interfaces can be used in executor
     * check ScheduledThreadPoolExecutor for more information
     */

    public ScheduledFuture<?> schedule(Runnable command,
                                       long delay, TimeUnit unit) {
        return this.executor.schedule(command, delay, unit);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable,
                                           long delay, TimeUnit unit) {
        return this.executor.schedule(callable, delay, unit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit) {
        return this.executor.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit) {
        return this.executor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    public void execute(Runnable command) {
        this.executor.execute(command);
    }

    public Future<?> submit(Runnable task) {
        return this.executor.submit(task);
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return this.executor.submit(task, result);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return this.executor.submit(task);
    }



}
