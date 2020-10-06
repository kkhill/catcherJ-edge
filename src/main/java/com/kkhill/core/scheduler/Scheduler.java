package com.kkhill.core.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

public class Scheduler {



    private ExecutorService executor;

    private Scheduler() {
        executor = new ScheduledThreadPoolExecutor(10);
    }

    private static class Holder {
        private static Scheduler instance = new Scheduler();
    }

    public static Scheduler getInstance() {
        return Holder.instance;
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}
