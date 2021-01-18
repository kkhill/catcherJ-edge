package com.kkhill.addons.automation;

import com.kkhill.addons.automation.rule.Rule;
import com.kkhill.addons.automation.utils.RuleParser;
import com.kkhill.core.Catcher;
import com.kkhill.core.event.Event;
import com.kkhill.core.event.EventConsumer;
import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.plugin.Addon;
import com.kkhill.common.event.EventType;
import com.kkhill.core.thing.Thing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Automation implements Addon, EventConsumer {
    /**
     * Automation listens event, checks conditions and execute actions as an event consumer
     */

    private final Logger logger = LoggerFactory.getLogger(Automation.class);

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
            this.rules = new RuleParser().parseRules(rulesData);
            for(Thing rule : rules) Catcher.getThingMonitor().registerThing(rule);
        } catch (FileNotFoundException e) {
            logger.error("can not find rules.yaml");
            e.printStackTrace();
        } catch (IllegalThingException e) {
            e.printStackTrace();
        }

        // registry rule api



        // listen events
        Catcher.getEventBus().listen(EventType.STATE_UPDATED, this);
        Catcher.getEventBus().listen(EventType.PROPERTY_UPDATED, this);
        Catcher.getEventBus().listen(EventType.SERVICE_CALLED, this);

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
     * check conditions and execute actions
     * @param event
     */
    @Override
    public void handle(Event event) {

        for(Rule rule : this.rules) {
            if(!rule.getEvent().equals(event.getType())) continue;
            if(rule.checkConditions(event)) {
                logger.info("rule conditions satisfied: {}", rule.getFriendlyName());
                rule.executeActions();
            }
        }
    }
}
