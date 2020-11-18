package com.kkhill.addons.rulengine.condition;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.Property;

public class PropertyCondition<T> extends Condition {

    private String thing;
    private String property;
    private String operation;
    private Comparable<T> value;

    public PropertyCondition(String thing, String property, String operation, Comparable<T> value) {
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

    @Override
    @SuppressWarnings("unchecked")
    public boolean check() {

        boolean res = false;
        try {
            Property<T> property = Catcher.getThingMonitor().getThing(this.thing).getProperty(this.property);
            if(
                (this.operation.equals("==") && this.value.compareTo((T) property.getValue()) == 0) ||
                (this.operation.equals(">=") && this.value.compareTo((T) property.getValue()) <= 0) ||
                (this.operation.equals(">") && this.value.compareTo((T) property.getValue()) < 0) ||
                (this.operation.equals("<=") && this.value.compareTo((T) property.getValue()) >= 0) ||
                (this.operation.equals("<") && this.value.compareTo((T) property.getValue()) > 0)
            ) res = true;

        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return res;
    }
}
