package com.kkhill.core.thing;

import com.kkhill.common.thing.CommonThing;

public class SystemThing extends Thing {
    public SystemThing(String name, String description) {
        super(CommonThing.SYSTEM, name, description);
    }

    public SystemThing(String name, String description, boolean available) {
        super(CommonThing.SYSTEM, name, description, available);
    }
}
