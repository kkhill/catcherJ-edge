package com.kkhill.core.thing;

import com.kkhill.common.thing.CommonThing;

public abstract class AddonThing extends Thing {
    public AddonThing(String name, String description) {
        super(CommonThing.ADDON, name, description);
    }

    public AddonThing(String name, String description, boolean available) {
        super(CommonThing.ADDON, name, description, available);
    }
}
