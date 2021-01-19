package com.kkhill.core.thing;

import com.kkhill.common.thing.ThingType;

public abstract class DriverThing extends Thing {
    public DriverThing(String name, String description) {
        super(ThingType.DRIVER, name, description);
    }

    public DriverThing(String name, String description, boolean available) {
        super(ThingType.DRIVER, name, description, available);
    }
}
