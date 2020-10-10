package com.kkhill;

import com.kkhill.core.Catcher;

public class Launcher {

    public static void main(String[] args) {

        // read configuration
        int threadNum = 5;
        int pollingInternal = 10;
        int heartbeat = 5;

        Catcher.initialize(threadNum, pollingInternal, heartbeat);
        Catcher.start();
    }
}
