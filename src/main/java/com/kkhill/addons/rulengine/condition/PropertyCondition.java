package com.kkhill.addons.rulengine.condition;

import com.kkhill.addons.rulengine.utils.RuleElement;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.Property;
import com.kkhill.core.thing.Thing;

public class PropertyCondition<T> extends Condition {

    private String thing;
    private String property;
    private String operation;
    private Comparable<T> value;

    public PropertyCondition(String conditionType, String thing, String property, String operation, Comparable<T> value) {
        super(conditionType);
        this.thing = thing;
        this.property = property;
        this.operation = operation;
        this.value = value;
    }

    public String getThing() {
        return thing;
    }

    public String getProperty() {
        return property;
    }

    public String getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean check() {

        boolean res = false;
        try {
            Thing thing = Catcher.getThingMonitor().getThing(this.thing);
            Property property = thing.getProperty(this.property);
            if(
                (this.operation.equals("==") && this.value.compareTo((T) property.getValue()) == 0) ||
                (this.operation.equals(">=") && this.value.compareTo((T) property.getValue()) >= 0) ||
                (this.operation.equals(">") && this.value.compareTo((T) property.getValue()) > 0) ||
                (this.operation.equals("<=") && this.value.compareTo((T) property.getValue()) <= 0) ||
                (this.operation.equals("<") && this.value.compareTo((T) property.getValue()) < 0)
            ) res = true;

        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return res;
    }
}
