package com.kkhill.core.thing;

import com.kkhill.common.thing.CThing;

public abstract class DriverThing extends Thing {
    public DriverThing(String name, String description) {
        super(CThing.DRIVER, name, description);
    }

    public DriverThing(String name, String description, boolean available) {
        super(CThing.DRIVER, name, description, available);
    }
}
