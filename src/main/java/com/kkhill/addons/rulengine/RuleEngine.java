package com.kkhill.addons.rulengine;

import com.kkhill.addons.rulengine.helper.*;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.Addon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class RuleEngine implements Addon {

    private final Logger logger = LoggerFactory.getLogger(RuleEngine.class);

    /**
     * Default path of rules.yaml
     */
    private String ruleFilePath = "src/main/resources/rules.yaml";

    /**
     * All rules loaded from rules.yaml
     */
    private List<Rule> rules;

    /**
     * Read rules.yaml
     * Create rule object
     * Listen to event bus
     * @param data config
     * @return true if all rules were loaded
     */
    @Override
    public boolean load(Object data) {
        try {
            List<LinkedHashMap<String, Object>> rulesData = readRules();
            this.rules = parseRules(rulesData);
        }catch (FileNotFoundException e) {
            logger.error("can not find rules.yaml");
            e.printStackTrace();
        } catch (IllegalRuleException e) {
            logger.error("illegal rule");
            e.printStackTrace();
        } catch (IllegalThingException e) {
            logger.error("illegal thing");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Clear all stored rules
     * Cancel listening to event bus
     * @param data config
     * @return true
     */
    @Override
    public boolean unload(Object data) {
        if(this.rules!=null) this.rules.clear();
        return true;
    }

    public void setRuleFilePath(String path) {
        this.ruleFilePath = path;
    }

    public String getRuleFilePath() {
        return this.ruleFilePath;
    }

    private List<LinkedHashMap<String, Object>> readRules() throws FileNotFoundException {
        return new Yaml().load(new FileInputStream(new File(ruleFilePath)));
    }

    /**
     * Parse event, conditions(states, properties) and actions(services)
     * @param data origin data read from rules.yaml by snakeyaml
     * @return rule object list
     */
    @SuppressWarnings("unchecked")
    private List<Rule> parseRules(List<LinkedHashMap<String, Object>> data) throws IllegalRuleException, IllegalThingException {
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
                            condition = new StateCondition((String)cc.get("thing"), (String)cc.get("on"));
                        } else {
                            condition = new StateCondition((String)cc.get("thing"), (String)cc.get("from"), (String)cc.get("to"));
                        }

                    } else if("properties".equals(key)) {
                        // property condition item
                        String op = cc.containsKey("equal") ? "equal" : cc.containsKey("greater") ? "greater" : cc.containsKey("less") ? "less" : null;
                        if(op == null) continue;
                        condition = new PropertyCondition((String)cc.get("thing"), (String)cc.get("property"),
                                op, cc.get("value"));
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
                        action = new ServiceAction((String)aa.get("name"), (String)aa.get("thing"));
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
}
