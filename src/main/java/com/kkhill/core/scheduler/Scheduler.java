package com.kkhill.core.scheduler;

import com.kkhill.core.thing.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Scheduler {

    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);

    private Queue<Service> polledServices = new ConcurrentLinkedQueue<>();
    private Queue<Service> pushedServices = new ConcurrentLinkedQueue<>();

    public void start() {



    }

    public void pollAll() {

        for(Service service : polledServices) {
            executor.submit(() -> service.invoke());
        }
    }

}
