package com.kkhill.core.thing;

import com.kkhill.common.thing.ThingType;

public abstract class AddonThing extends Thing {
    public AddonThing(String name, String description) {
        super(ThingType.ADDON, name, description);
    }

    public AddonThing(String name, String description, boolean available) {
        super(ThingType.ADDON, name, description, available);
    }
}
