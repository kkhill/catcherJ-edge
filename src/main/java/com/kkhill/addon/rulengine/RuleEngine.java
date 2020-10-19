package com.kkhill.addon.rulengine;

import com.kkhill.core.plugin.Addon;

public class RuleEngine implements Addon {

    @Override
    public boolean load(Object data) {
        System.out.println("load rulengine");
        // TODO read rule.yaml -> create Rule object -> listen to event bus.  handle event to check conditions and execute actions
        return false;
    }

    @Override
    public boolean unload(Object data) {
        return false;
    }
}
