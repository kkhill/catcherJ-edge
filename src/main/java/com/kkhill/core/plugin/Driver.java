package com.kkhill.core.plugin;

/**
 * Driver has some specific stages of lifecycle about things
 */
public abstract class Driver implements Plugin {

    @Override
    public final void load() {
        discover();
        initialize();
    }

    @Override
    public final void unload() {
        release();
    }

    public abstract void discover();

    public abstract void initialize();

    public abstract void release();
}
