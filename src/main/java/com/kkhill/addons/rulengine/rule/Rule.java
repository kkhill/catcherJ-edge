package com.kkhill.addons.rulengine.rule;

import com.kkhill.addons.rulengine.action.Action;
import com.kkhill.addons.rulengine.condition.Condition;
import com.kkhill.core.event.Event;
import com.kkhill.utils.thing.StateName;
import com.kkhill.core.annotation.State;
import com.kkhill.core.thing.Thing;

import java.util.List;

public class Rule extends Thing {

    private String event;
    private List<Condition> conditions;
    private List<Action> actions;

    @State(description = "state")
    private String state;

    public Rule(String type, String friendlyName, String description,
                String event, List<Condition> conditions, List<Action> actions) {
        super(type, friendlyName, description);
        this.event = event;
        this.conditions = conditions;
        this.actions = actions;
        this.state = StateName.ON;
    }

    public boolean checkConditions(Event event) {
        for(Condition condition : this.getConditions()) {
            if(!condition.check(event.getData())) {
                return false;
            }
        }
        return true;
    }

    public void executeActions() {
        for(Action action : this.getActions()) {
            action.execute();
        }
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
