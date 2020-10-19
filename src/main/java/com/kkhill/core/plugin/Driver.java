package com.kkhill.core.plugin;

import java.util.LinkedHashMap;

/**
 * Driver has some specific stages of lifecycle about things
 */
public abstract class Driver implements Plugin {

    protected Object config;

    public Object getConfig() {
        return this.config;
    }

    @Override
    public final boolean load(Object data) {
        this.config = discover(data);
        initialize(this.config);
        return true;
    }

    @Override
    public final boolean unload(Object data) {
        release();
        return true;
    }

    public abstract Object discover(Object data);

    public abstract void initialize(Object data);

    public abstract void release();
}
