package com.kkhill.core.thing;

public abstract class DeviceThing extends Thing{
    public DeviceThing(String type, String name, String description) {
        super(type, name, description);
    }

    public DeviceThing(String type, String name, String description, boolean available) {
        super(type, name, description, available);
    }
}
