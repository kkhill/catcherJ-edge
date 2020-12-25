package com.kkhill.addons.automation.rule;

import com.kkhill.addons.automation.action.Action;
import com.kkhill.addons.automation.condition.Condition;
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
    public String state;

    public Rule(String type, String name, String description,
                String event, List<Condition> conditions, List<Action> actions) {
        super(type, name, description);
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
