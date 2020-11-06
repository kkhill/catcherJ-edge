package com.kkhill.addons.rulengine.helper;

import com.kkhill.utils.convention.StateName;
import com.kkhill.core.annotation.State;
import com.kkhill.core.thing.Thing;

import java.util.List;

public class Rule extends Thing {

    private String event;
    private List<Condition> conditions;
    private List<Action> actions;

    @State(description = "state")
    private String state;

    public Rule(String friendlyName, boolean available, String event, List<Condition> conditions, List<Action> actions) {
        super(friendlyName, available);
        this.event = event;
        this.conditions = conditions;
        this.actions = actions;
        this.state = StateName.ON;
    }

    public boolean checkConditions() {

        return true;
    }

    public void executeActions() {

    }

    public String getEvent() {
        return event;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<Action> getActions() {
        return actions;
    }
}
