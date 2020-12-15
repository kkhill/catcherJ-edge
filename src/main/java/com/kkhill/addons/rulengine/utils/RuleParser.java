package com.kkhill.addons.rulengine.utils;

import com.kkhill.addons.rulengine.action.Action;
import com.kkhill.addons.rulengine.action.ServiceAction;
import com.kkhill.addons.rulengine.condition.Condition;
import com.kkhill.addons.rulengine.condition.PropertyCondition;
import com.kkhill.addons.rulengine.condition.StateCondition;
import com.kkhill.addons.rulengine.rule.IllegalRuleException;
import com.kkhill.addons.rulengine.rule.Rule;
import com.kkhill.utils.thing.ThingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class RuleParser {

    private final Logger logger = LoggerFactory.getLogger(RuleParser.class);

    public List<Rule> parseRules(List<LinkedHashMap<String, Object>> data) {
        List<Rule> rules = new ArrayList<>();
        for (LinkedHashMap<String, Object> entry : data) {
            try {
                rules.add(parseRule(entry));
            } catch (IllegalRuleException e) {
                logger.error("parse rule failed");
                e.printStackTrace();
            }

        }
        return rules;
    }

    @SuppressWarnings("unchecked")
    public Rule parseRule(LinkedHashMap<String, Object> data) throws IllegalRuleException {

        if(!data.containsKey("name")) throw new IllegalRuleException("no rule name");
        if(!data.containsKey("description")) throw new IllegalRuleException("no description");
        if(!data.containsKey("event")) throw new IllegalRuleException("no event");
        if(!data.containsKey("conditions")) throw new IllegalRuleException("no conditions");
        if(!data.containsKey("actions")) throw new IllegalRuleException("no actions");

        String name = (String) data.get("name");
        String event = (String) data.get("event");
        String description = (String) data.get("description");
        List<Condition> conditions = parseConditions((LinkedHashMap<String, Object>) data.get("conditions"));
        List<Action> actions = parseActions((LinkedHashMap<String, Object>) data.get("actions"));

        return new Rule(ThingType.RULE, name, description, event, conditions, actions);
    }

    @SuppressWarnings("unchecked")
    public List<Condition> parseConditions(LinkedHashMap<String, Object> data) throws IllegalRuleException {

        if(data == null) throw new IllegalRuleException("empty conditions");
        List<Condition> conditions = new ArrayList<>();

        for(String key: data.keySet()) {
            List<LinkedHashMap<String, Object>> c =  (List<LinkedHashMap<String, Object>>) data.get(key);
            for(LinkedHashMap<String, Object> cc : c) {
                Condition condition = null;
                if(RuleElement.STATES.equals(key)) {
                    // state condition item
                    if(cc.containsKey(RuleElement.ON)) {
                        condition = new StateCondition((String)cc.get("thing"), (String)cc.get(RuleElement.ON));
                    } else {
                        condition = new StateCondition((String)cc.get("thing"), (String)cc.get(RuleElement.FROM), (String)cc.get(RuleElement.TO));
                    }

                } else if(RuleElement.PROPERTIES.equals(key)) {
                    // property condition item
                    String op = (String) cc.get(RuleElement.OPERATION);
                    if(op == null) throw new IllegalRuleException("missing property comparable operation");
                    condition = new PropertyCondition<>((String)cc.get("thing"), (String)cc.get("property"),
                            op, (Comparable<Object>) cc.get("value"));
                }
                if(condition != null) conditions.add(condition);
            }
        }

        return conditions;
    }

    @SuppressWarnings("unchecked")
    public List<Action> parseActions(LinkedHashMap<String, Object> data) throws IllegalRuleException {

        if(data == null) { throw new IllegalRuleException("empty actions"); }
        List<Action> actions = new ArrayList<>();
        for(String key: data.keySet()) {
            List<LinkedHashMap<String, Object>> a =  (List<LinkedHashMap<String, Object>>) data.get(key);
            for(LinkedHashMap<String, Object> aa : a) {
                Action action = null;
                if("services".equals(key)) {
                    action = new ServiceAction((String)aa.get("name"), (String)aa.get("thing"));
                }
                actions.add(action);
            }
        }
        return actions;
    }
}
