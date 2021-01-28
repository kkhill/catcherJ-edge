package com.kkhill.core.thing;

import com.kkhill.common.thing.CThing;

public abstract class AddonThing extends Thing {
    public AddonThing(String name, String description) {
        super(CThing.ADDON, name, description);
    }

    public AddonThing(String name, String description, boolean available) {
        super(CThing.ADDON, name, description, available);
    }
}
