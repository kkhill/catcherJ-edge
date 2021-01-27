package com.kkhill.addons.automation.utils;

import com.kkhill.addons.automation.action.Action;
import com.kkhill.addons.automation.action.ServiceAction;
import com.kkhill.addons.automation.condition.Condition;
import com.kkhill.addons.automation.condition.PropertyCondition;
import com.kkhill.addons.automation.condition.StateCondition;
import com.kkhill.addons.automation.rule.IllegalRuleException;
import com.kkhill.addons.automation.rule.Rule;
import com.kkhill.addons.automation.trigger.PropertyUpdatedTrigger;
import com.kkhill.addons.automation.trigger.StateUpdatedTrigger;
import com.kkhill.addons.automation.trigger.Trigger;
import com.kkhill.common.event.EventType;
import com.kkhill.common.thing.ThingUtil;

import java.util.*;

public class RuleUtil {

    @SuppressWarnings("unchecked")
    public static boolean compare(String op, Comparable a, Comparable b) {
        return (
                (op.equals("==") && a.compareTo(b) == 0) ||
                        (op.equals(">=") && a.compareTo(b) <= 0) ||
                        (op.equals(">") && a.compareTo(b) < 0) ||
                        (op.equals("<=") && a.compareTo(b) >= 0) ||
                        (op.equals("<") && a.compareTo(b) > 0)
        );
    }

    public static List<Rule> parseRules(List<LinkedHashMap<String, Object>> data) throws IllegalRuleException {
        List<Rule> rules = new ArrayList<>();
        for (LinkedHashMap<String, Object> entry : data) rules.add(parseRule(entry));
        return rules;
    }

    @SuppressWarnings("unchecked")
    public static Rule parseRule(Map<String, Object> data) throws IllegalRuleException {

        if(!data.containsKey(RuleElement.NAME)) throw new IllegalRuleException("no rule name");
        if(!data.containsKey(RuleElement.DESCRIPTION)) throw new IllegalRuleException("no description");
        if(!data.containsKey(RuleElement.TRIGGER)) throw new IllegalRuleException("no trigger");
        if(!data.containsKey(RuleElement.CONDITIONS)) throw new IllegalRuleException("no conditions");
        if(!data.containsKey(RuleElement.ACTIONS)) throw new IllegalRuleException("no actions");

        String name = (String) data.get(RuleElement.NAME);
        String description = (String) data.get(RuleElement.DESCRIPTION);
        Trigger trigger = parseTrigger((Map<String, Object>) data.get(RuleElement.TRIGGER));
        List<Condition> conditions = parseConditions((Map<String, Object>) data.get(RuleElement.CONDITIONS));
        List<Action> actions = parseActions((Map<String, Object>) data.get(RuleElement.ACTIONS));
        return new Rule(name, description, trigger, conditions, actions);
    }

    @SuppressWarnings("unchecked")
    public static Trigger parseTrigger(Map<String, Object> data) {

        if(data.get(RuleElement.EVENT).equals(EventType.STATE_UPDATED)) {
            return new StateUpdatedTrigger(
                    (String)data.get(RuleElement.THING),
                    (String)data.get(RuleElement.DESCRIPTION),
                    (String)data.get(RuleElement.FROM),
                    (String)data.get(RuleElement.TO));
        } else if(data.get(RuleElement.EVENT).equals(EventType.PROPERTY_UPDATED)) {
            Map<String, Object> from = (Map<String, Object>)data.get(RuleElement.FROM);
            Map<String, Object> to = (Map<String, Object>)data.get(RuleElement.TO);
            return new PropertyUpdatedTrigger(
                    (String)data.get(RuleElement.THING),
                    (String)data.get(RuleElement.DESCRIPTION),
                    (String)data.get(RuleElement.PROPERTY),
                    (String)from.get(RuleElement.OPERATION),
                    (Comparable)from.get(RuleElement.VALUE),
                    (String)to.get(RuleElement.OPERATION),
                    (Comparable)to.get(RuleElement.VALUE));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static List<Condition> parseConditions(Map<String, Object> data) {

        List<Condition> conditions = new ArrayList<>();
        if(data == null) return conditions;

        if(data.get(RuleElement.STATES) != null) {
            List<Map<String, String>> states = (List<Map<String, String>>) data.get(RuleElement.STATES);
            for(Map<String, String> state : states) {
                conditions.add(new StateCondition(
                        state.get(RuleElement.THING),
                        state.get(RuleElement.STATE),
                        state.get(RuleElement.DESCRIPTION)));
            }
        }

        if(data.get(RuleElement.PROPERTIES) != null) {
            List<Map<String, Object>> properties = (List<Map<String, Object>>) data.get(RuleElement.PROPERTIES);
            for(Map<String, Object> property : properties) {
                conditions.add(new PropertyCondition(
                        (String)property.get(RuleElement.THING),
                        (String)property.get(RuleElement.PROPERTY),
                        (String)property.get(RuleElement.OPERATION),
                        (Comparable) property.get(RuleElement.VALUE),
                        (String)property.get(RuleElement.DESCRIPTION)));
            }
        }

        return conditions;
    }

    @SuppressWarnings("unchecked")
    public static List<Action> parseActions(Map<String, Object> data) {

        List<Action> actions = new ArrayList<>();
        if(data == null) return actions;
        if(data.get(RuleElement.SERVICES) != null) {
            List<Map<String, Object>> services = (List<Map<String, Object>>) data.get(RuleElement.SERVICES);
            for(Map<String, Object> service : services) {
                Map<String, Object> params = new HashMap<>();
                List<Map<String, Object>> sp = (List<Map<String, Object>>) service.get(RuleElement.PARAMS);
                if(sp!=null&&sp.size()!=0) {
                    for(Map<String, Object> p : sp) {
                        params.put((String)p.get("name"),
                                ThingUtil.deserializeServiceParams(p.get("value"), (String)p.get("type")));
                    }
                }
                actions.add(new ServiceAction(
                        (String)service.get(RuleElement.NAME),
                        (String)service.get(RuleElement.THING),
                        (String)service.get(RuleElement.DESCRIPTION),
                        params));
            }
        }

        return actions;
    }
}
