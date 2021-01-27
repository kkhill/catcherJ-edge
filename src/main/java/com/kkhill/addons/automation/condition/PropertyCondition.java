package com.kkhill.addons.automation.condition;

import com.kkhill.addons.automation.utils.Utils;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;

public class PropertyCondition extends Condition {

    private String property;
    private String operation;
    private Comparable value;

    public PropertyCondition(String thing, String property, String operation,
                             Comparable value, String description) {
        super(thing, description);
        this.property = property;
        this.operation = operation;
        this.value = value;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public boolean check() {
        try {
            Comparable data = Catcher.getThingMonitor().getThing(thing).getProperty(property).getValue();
            return Utils.compare(this.operation, this.value, data);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return "PropertyCondition{" +
                "property='" + property + '\'' +
                ", operation='" + operation + '\'' +
                ", value=" + value +
                '}';
    }
}
