package com.kkhill.addons.rulengine.condition;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.utils.event.dto.StateUpdatedEventData;

public class StateCondition extends Condition{

    private String thing;
    private String from;
    private String to;
    private String on;

    public StateCondition(String thing, String from, String to) {
        this.thing = thing;
        this.from = from;
        this.to = to;
    }

    public StateCondition(String thing, String on) {
        this.thing = thing;
        this.on = on;
    }

    public String getThing() {
        return thing;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getOn() {
        return on;
    }

    @Override
    public boolean check(Object data) {

        StateUpdatedEventData d = (StateUpdatedEventData)data;
        if(d.getThingId().equals(this.thing)) {
            try {
                if(Catcher.getThingMonitor().getThing(this.thing).getState().getValue().equals(this.on)) return true;
                if(d.getOldState().equals(this.from) && d.getNewState().equals(this.to)) return true;
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
