package com.kkhill.addons.automation.condition;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.common.event.dto.StateUpdatedEventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StateCondition extends Condition {

    private final Logger logger = LoggerFactory.getLogger(StateCondition.class);

    private String from;
    private String to;
    private String stay;

    public StateCondition(String thing, String from, String to, String description) {
        super(thing, description);
        this.from = from;
        this.to = to;
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

        // stay condition
        try {
            if(Catcher.getThingMonitor().getThing(this.thing).getState().getValue().equals(this.stay)) return true;
        } catch (NotFoundException e) {
            logger.error("not found thing: {}", this.thing) ;
        }

        // from to condition
        StateUpdatedEventData d = (StateUpdatedEventData)data;
        if(d.getId().equals(this.thing) &&
                d.getOldState().equals(this.from) &&
                d.getNewState().equals(this.to)) {

            return true;
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
