package com.kkhill.addons.automation.rule;

import com.kkhill.addons.automation.action.Action;
import com.kkhill.addons.automation.condition.Condition;
import com.kkhill.addons.automation.trigger.Trigger;
import com.kkhill.common.thing.CThing;
import com.kkhill.core.Catcher;
import com.kkhill.core.annotation.Property;
import com.kkhill.core.annotation.Service;
import com.kkhill.common.thing.CState;
import com.kkhill.core.annotation.State;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.Thing;

import java.util.List;

public class Rule extends Thing {

    private Trigger trigger;
    private List<Condition> conditions;
    private List<Action> actions;

    @State(description = "state")
    public String state = CState.ON;

    @Property(name="trigger", description="trigger event type")
    public String tri;

    @Property(name="conditions", description="conditions detail")
    public String con;

    @Property(name="actions", description = "actions in detail")
    public String act;

    @Service(name="toggle", description = "使用/禁止规则")
    public void toggle() {
        if(this.state.equals(CState.ON)) this.state = CState.OFF;
        else this.state = CState.ON;
        try {
            Catcher.getThingMonitor().updateStateAndNotify(this.getId());
        } catch (NotFoundException | IllegalThingException e) {
            e.printStackTrace();
        }
    }

    public Rule(String name, String description,
                Trigger trigger, List<Condition> conditions, List<Action> actions) {
        super(CThing.RULE, name, description);
        this.trigger = trigger;
        this.conditions = conditions;
        this.actions = actions;
        this.state = CState.ON;


        // construct details of trigger, conditions and actions
        tri = trigger.getDescription();
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

    public boolean checkTrigger(String thing, Object from, Object to) {
        return trigger.check(thing, from, to);
    }

    public boolean checkConditions() {
        for(Condition condition : this.getConditions()) {
            if(!condition.check()) {
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

    public Trigger getTrigger() {
        return trigger;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public List<Action> getActions() {
        return actions;
    }

}
