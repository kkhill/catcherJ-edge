package com.kkhill.addons.automation.condition;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.common.event.dto.StateUpdatedEventData;

public class StateCondition extends Condition{

    private String from;
    private String to;
    private String stay;

    public StateCondition(String thing, String from, String to, String description) {
        super(thing, description);
        this.from = from;
        this.to = to;
        if(description == null || "".equals(description))
            this.description = this.toString();
    }

    public StateCondition(String thing, String stay, String description) {
        super(thing, description);
        this.stay = stay;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getOn() {
        return stay;
    }

    @Override
    public boolean check(Object data) {

        StateUpdatedEventData d = (StateUpdatedEventData)data;
        try {
            if(Catcher.getThingMonitor().getThing(this.thing).getState().getValue().equals(this.stay)) return true;
            // TODO: the event must triggered by this thing to check from ... to ...
            if(d.getOldState().equals(this.from) && d.getNewState().equals(this.to)) return true;
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        if(this.stay!=null) {
            return thing+".state stay "+stay;
        } else {
            return thing+".state from "+from+" to "+to;
        }
    }
}
