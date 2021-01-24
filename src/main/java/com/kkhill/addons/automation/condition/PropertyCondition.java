package com.kkhill.addons.automation.condition;

import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.element.Property;

public class PropertyCondition<T> extends Condition {

    private String name;
    private String operation;
    private Comparable<T> value;

    public PropertyCondition(String thing, String property, String operation, Comparable<T> value, String description) {
        super(thing, description);
        this.name = property;
        this.operation = operation;
        this.value = value;
    }

    public String getProperty() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean check(Object data) {

        boolean res = false;
        try {
            Property<T> property = Catcher.getThingMonitor().getThing(this.thing).getProperty(this.name);
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

    @Override
    public String toString() {
        return thing+"."+name+" "+operation+" "+value;
    }
}
