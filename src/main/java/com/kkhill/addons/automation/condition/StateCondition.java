package com.kkhill.addons.automation.condition;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;

public class StateCondition extends Condition {

    private String state;

    public StateCondition(String thing, String state, String description) {
        super(thing, description);
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public boolean check() {
        try {
            String state = Catcher.getThingMonitor().getThing(thing).getState().getValue();
            return this.state.equals(state);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return "StateCondition{" +
                "state='" + state + '\'' +
                '}';
    }
}
