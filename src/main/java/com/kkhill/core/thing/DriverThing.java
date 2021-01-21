package com.kkhill.core.thing;

import com.kkhill.common.thing.CommonThing;

public abstract class DriverThing extends Thing {
    public DriverThing(String name, String description) {
        super(CommonThing.DRIVER, name, description);
    }

    public DriverThing(String name, String description, boolean available) {
        super(CommonThing.DRIVER, name, description, available);
    }
}
