package com.kkhill.core.plugin;

public interface Plugin {

    boolean load(Object data);
    boolean unload(Object data);
}
