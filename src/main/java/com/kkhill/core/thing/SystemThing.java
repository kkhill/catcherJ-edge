package com.kkhill.core.thing;

import com.kkhill.common.thing.CThing;

public abstract class SystemThing extends Thing {
    public SystemThing(String name, String description) {
        super(CThing.SYSTEM, name, description);
    }

    public SystemThing(String name, String description, boolean available) {
        super(CThing.SYSTEM, name, description, available);
    }
}
