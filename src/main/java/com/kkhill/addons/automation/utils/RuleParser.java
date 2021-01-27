package com.kkhill.addons.automation.utils;

import com.kkhill.addons.automation.action.Action;
import com.kkhill.addons.automation.action.ServiceAction;
import com.kkhill.addons.automation.condition.Condition;
import com.kkhill.addons.automation.condition.PropertyCondition;
import com.kkhill.addons.automation.condition.StateCondition;
import com.kkhill.addons.automation.rule.IllegalRuleException;
import com.kkhill.addons.automation.rule.Rule;
import com.kkhill.common.thing.ThingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RuleParser {

    private final Logger logger = LoggerFactory.getLogger(RuleParser.class);

    class RuleElement {
        public static final String NAME = "name";
        public static final String EVENT = "event";
        public static final String CONDITIONS = "conditions";
        public static final String STATES = "states";
        public static final String THING = "thing";
        public static final String FROM = "from";
        public static final String TO = "to";
        public static final String STAY = "stay";
        public static final String PROPERTIES = "properties";
        public static final String PROPERTY = "property";
        public static final String OPERATION = "operation";
        public static final String ACTIONS = "actions";
        public static final String SERVICES = "services";
        public static final String DESCRIPTION = "description";
        public static final String VALUE = "value";
        public static final String PARAMS = "params";
        public static final String TYPE = "type";
        public static final String OLDVALUE = "oldValue";
        public static final String NEWVALUE = "newValue";
        public static final String OLDOPERATION = "oldOperation";
        public static final String NEWOPERATION = "newOperation";
    }

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

        if(!data.containsKey(RuleElement.NAME)) throw new IllegalRuleException("no rule name");
        if(!data.containsKey(RuleElement.DESCRIPTION)) throw new IllegalRuleException("no description");
        if(!data.containsKey(RuleElement.EVENT)) throw new IllegalRuleException("no event");
        if(!data.containsKey(RuleElement.CONDITIONS)) throw new IllegalRuleException("no conditions");
        if(!data.containsKey(RuleElement.ACTIONS)) throw new IllegalRuleException("no actions");

        String name = (String) data.get(RuleElement.NAME);
        String event = (String) data.get(RuleElement.EVENT);
        String description = (String) data.get(RuleElement.DESCRIPTION);
        List<Condition> conditions = parseConditions((LinkedHashMap<String, Object>) data.get(RuleElement.CONDITIONS));
        List<Action> actions = parseActions((LinkedHashMap<String, Object>) data.get(RuleElement.ACTIONS));

        return new Rule(name, description, event, conditions, actions);
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
                    if(cc.containsKey(RuleElement.STAY)) {
                        condition = new StateCondition((String)cc.get(RuleElement.THING), (String)cc.get(RuleElement.STAY),
                                (String)cc.get(RuleElement.DESCRIPTION));
                    } else {
                        condition = new StateCondition((String)cc.get(RuleElement.THING), (String)cc.get(RuleElement.FROM),
                                (String)cc.get(RuleElement.TO), (String)cc.get(RuleElement.DESCRIPTION));
                    }

                } else if(RuleElement.PROPERTIES.equals(key)) {
                    // property condition item
                    if(cc.get(RuleElement.OPERATION)!=null) {
                        condition = new PropertyCondition<>((String)cc.get(RuleElement.THING), (String)cc.get(RuleElement.PROPERTY),
                                (String)cc.get(RuleElement.OPERATION), (Comparable<Object>) cc.get(RuleElement.VALUE), (String)cc.get(RuleElement.DESCRIPTION));
                    } else {
                        condition = new PropertyCondition<>((String)cc.get(RuleElement.THING), (String)cc.get(RuleElement.PROPERTY),
                                (String)cc.get(RuleElement.OLDOPERATION), (Comparable<Object>) cc.get(RuleElement.OLDVALUE),
                                (String)cc.get(RuleElement.NEWOPERATION), (Comparable<Object>) cc.get(RuleElement.NEWVALUE),
                                (String)cc.get(RuleElement.DESCRIPTION)
                        );
                    }

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
                if(RuleElement.SERVICES.equals(key)) {
                    // parse Service Params
                    Map<String, Object> params = new HashMap<>();
                    List<Map<String, Object>> sp = (List<Map<String, Object>>) aa.get(RuleElement.PARAMS);
                    if(sp!=null&&sp.size()!=0) {
                        for(Map<String, Object> p : sp) {
                            params.put((String)p.get("name"),
                                    ThingUtil.deserializeServiceParams(p.get("value"), (String)p.get("type")));
                        }
                    }
                    Action action = new ServiceAction((String)aa.get(RuleElement.NAME), (String)aa.get(RuleElement.THING),
                    (String)aa.get(RuleElement.DESCRIPTION), params);
                    actions.add(action);
                }

            }
        }
        return actions;
    }
}
