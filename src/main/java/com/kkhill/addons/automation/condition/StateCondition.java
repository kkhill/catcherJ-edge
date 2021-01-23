package com.kkhill.addons.automation.condition;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.common.event.dto.StateUpdatedEventData;

public class StateCondition extends Condition{

    private String from;
    private String to;
    private String on;

    public StateCondition(String thing, String from, String to, String description) {
        super(thing, description);
        this.from = from;
        this.to = to;
        if(description == null || "".equals(description))
            this.description = this.toString();
    }

    public StateCondition(String thing, String on, String description) {
        super(thing, description);
        this.on = on;
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
        if(d.getId().equals(this.thing)) {
            try {
                if(Catcher.getThingMonitor().getThing(this.thing).getState().getValue().equals(this.on)) return true;
                if(d.getOldState().equals(this.from) && d.getNewState().equals(this.to)) return true;
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String toString() {
        if(this.on!=null) {
            return thing+".state on "+on;
        } else {
            return thing+".state from "+from+" to "+to;
        }
    }
}
