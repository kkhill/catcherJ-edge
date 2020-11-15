package com.kkhill.addons.rulengine.utils;

import com.kkhill.addons.rulengine.action.Action;
import com.kkhill.addons.rulengine.action.ActionType;
import com.kkhill.addons.rulengine.action.ServiceAction;
import com.kkhill.addons.rulengine.condition.Condition;
import com.kkhill.addons.rulengine.condition.ConditionType;
import com.kkhill.addons.rulengine.condition.PropertyCondition;
import com.kkhill.addons.rulengine.condition.StateCondition;
import com.kkhill.addons.rulengine.rule.IllegalRuleException;
import com.kkhill.addons.rulengine.rule.Rule;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RuleParser {

    @SuppressWarnings("unchecked")
    public List<Rule> parseRules(List<LinkedHashMap<String, Object>> data) throws IllegalRuleException, IllegalThingException {
        List<Rule> rules = new ArrayList<>();
        for (LinkedHashMap<String, Object> entry : data) {

            if(!entry.containsKey("name")) throw new IllegalRuleException("no rule name");
            if(!entry.containsKey("event")) throw new IllegalRuleException("no event");
            if(!entry.containsKey("conditions")) throw new IllegalRuleException("no conditions");
            if(!entry.containsKey("actions")) throw new IllegalRuleException("no actions");

            String name = (String) entry.get("name");
            String event = (String) entry.get("event");
            // parse conditions
            LinkedHashMap<String, Object> conditionData = (LinkedHashMap<String, Object>) entry.get("conditions");
            if(conditionData == null) {
                throw new IllegalRuleException("empty conditions");
            }
            List<Condition> conditions = new ArrayList<>();
            for(String key: conditionData.keySet()) {
                List<LinkedHashMap<String, Object>> c =  (List<LinkedHashMap<String, Object>>) conditionData.get(key);
                for(LinkedHashMap<String, Object> cc : c) {
                    Condition condition = null;
                    if("states".equals(key)) {
                        // state condition item
                        if(cc.containsKey("on")) {
                            condition = new StateCondition(ConditionType.STATE_CONDITION, (String)cc.get("thing"), (String)cc.get("on"));
                        } else {
                            condition = new StateCondition((String)cc.get("thing"), (String)cc.get("from"), (String)cc.get("to"));
                        }

                    } else if("properties".equals(key)) {
                        // property condition item
                        String op = cc.containsKey("equal") ? "equal" : cc.containsKey("greater") ? "greater" : cc.containsKey("less") ? "less" : null;
                        if(op == null) continue;
                        condition = new PropertyCondition(ConditionType.PROPERTY_CONDITION, (String)cc.get("thing"), (String)cc.get("property"),
                                op, (Comparable) cc.get("value"));
                    }
                    if(condition != null) conditions.add(condition);
                }
            }
            // parse actions
            LinkedHashMap<String, Map<String, Object>> actionData = (LinkedHashMap<String, Map<String, Object>>) entry.get("actions");
            if(conditionData == null) {
                throw new IllegalRuleException("empty actions");
            }
            List<Action> actions = new ArrayList<>();
            for(String key: actionData.keySet()) {
                List<LinkedHashMap<String, Object>> a =  (List<LinkedHashMap<String, Object>>) actionData.get(key);
                for(LinkedHashMap<String, Object> aa : a) {
                    Action action = null;
                    if("services".equals(key)) {
                        action = new ServiceAction(ActionType.SERVICE_ACTION, (String)aa.get("name"), (String)aa.get("thing"));
                    }
                    actions.add(action);
                }
            }
            Rule rule = new Rule(name, true, event, conditions, actions);
            // utilize ThingMonitor to manage rule
            Catcher.getThingMonitor().registerThing(rule);
            rules.add(rule);
        }
        return rules;
    }

    // TODO 类型推断
    @SuppressWarnings("unchecked")
    public Rule parseRule(LinkedHashMap<String, Object> data) throws IllegalRuleException {

        if(!data.containsKey("name")) throw new IllegalRuleException("no rule name");
        if(!data.containsKey("event")) throw new IllegalRuleException("no event");
        if(!data.containsKey("conditions")) throw new IllegalRuleException("no conditions");
        if(!data.containsKey("actions")) throw new IllegalRuleException("no actions");

        String name = (String) data.get("name");
        String event = (String) data.get("event");
        // parse conditions
        LinkedHashMap<String, Object> conditionData = (LinkedHashMap<String, Object>) data.get("conditions");
        if(conditionData == null) throw new IllegalRuleException("empty conditions");
        List<Condition> conditions = new ArrayList<>();

        for(String key: conditionData.keySet()) {
            List<LinkedHashMap<String, Object>> c =  (List<LinkedHashMap<String, Object>>) conditionData.get(key);
            for(LinkedHashMap<String, Object> cc : c) {
                Condition condition = null;
                if(RuleElement.STATES.equals(key)) {
                    // state condition item
                    if(cc.containsKey(RuleElement.ON)) {
                        condition = new StateCondition(ConditionType.STATE_CONDITION, (String)cc.get("thing"), (String)cc.get(RuleElement.ON));
                    } else {
                        condition = new StateCondition(ConditionType.STATE_CONDITION, (String)cc.get("thing"), (String)cc.get(RuleElement.FROM), (String)cc.get(RuleElement.TO));
                    }

                } else if(RuleElement.PROPERTIES.equals(key)) {
                    // property condition item
                    String op = (String) cc.get(RuleElement.OPERATION);
                    if(op == null) throw new IllegalRuleException("missing property comparable operation");



                    condition = new PropertyCondition(ConditionType.PROPERTY_CONDITION, (String)cc.get("thing"), (String)cc.get("property"),
                            op, (Comparable) cc.get("value"));
                }
                if(condition != null) conditions.add(condition);
            }
        }
        // parse actions
        LinkedHashMap<String, Map<String, Object>> actionData = (LinkedHashMap<String, Map<String, Object>>) data.get("actions");
        if(conditionData == null) {
            throw new IllegalRuleException("empty actions");
        }
        List<Action> actions = new ArrayList<>();
        for(String key: actionData.keySet()) {
            List<LinkedHashMap<String, Object>> a =  (List<LinkedHashMap<String, Object>>) actionData.get(key);
            for(LinkedHashMap<String, Object> aa : a) {
                Action action = null;
                if("services".equals(key)) {
                    action = new ServiceAction(ActionType.SERVICE_ACTION, (String)aa.get("name"), (String)aa.get("thing"));
                }
                actions.add(action);
            }
        }
        return new Rule(name, true, event, conditions, actions);
    }
}
