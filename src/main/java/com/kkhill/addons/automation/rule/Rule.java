package com.kkhill.addons.automation.rule;

import com.kkhill.addons.automation.action.Action;
import com.kkhill.addons.automation.condition.Condition;
import com.kkhill.common.thing.CommonThing;
import com.kkhill.core.Catcher;
import com.kkhill.core.annotation.Property;
import com.kkhill.core.annotation.Service;
import com.kkhill.core.event.Event;
import com.kkhill.common.thing.CommonState;
import com.kkhill.core.annotation.State;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.Thing;

import java.util.List;

public class Rule extends Thing {

    private List<Condition> conditions;
    private List<Action> actions;

    @State(description = "state")
    public String state = CommonState.ON;

    @Property(name="event", description="trigger event type")
    public String event;

    @Property(name="conditions", description="conditions detail")
    public String con;

    @Property(name="actions", description = "actions in detail")
    public String act;

    @Service(name="toggle", description = "使用/禁止规则")
    public void toggle() {
        if(this.state.equals(CommonState.ON)) this.state = CommonState.OFF;
        else this.state = CommonState.ON;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    public Rule(String name, String description,
                String event, List<Condition> conditions, List<Action> actions) {
        super(CommonThing.RULE, name, description);
        this.event = event;
        this.conditions = conditions;
        this.actions = actions;
        this.state = CommonState.ON;

        // construct details of conditions and actions
        StringBuilder conBuilder = new StringBuilder();
        for(int i=0; i<conditions.size(); i++) {
            if(i==0) conBuilder.append(conditions.get(0).getDescription());
            else conBuilder.append("\n").append(conditions.get(i).getDescription());
        }
        con = conBuilder.toString();
        StringBuilder actBuilder = new StringBuilder();
        for(int i=0; i<actions.size(); i++) {
            if(i==0) actBuilder.append(actions.get(0).getDescription());
            else actBuilder.append("\n").append(actions.get(i).getDescription());
        }
        act = actBuilder.toString();
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
