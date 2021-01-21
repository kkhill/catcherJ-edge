package com.kkhill.core.plugin;

/**
 * Addon could do sophisticated jobs during lifecycle of system
 */
public interface Addon extends Plugin {
    boolean start();
    boolean stop();
}
