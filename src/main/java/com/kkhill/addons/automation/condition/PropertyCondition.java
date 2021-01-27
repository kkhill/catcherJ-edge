package com.kkhill.addons.automation.condition;

import com.kkhill.common.event.dto.PropertyUpdatedEventData;
import com.kkhill.core.Catcher;
import com.kkhill.core.exception.NotFoundException;
import com.kkhill.core.thing.element.Property;

public class PropertyCondition<T> extends Condition {

    private String name;
    private String newOp;
    private String oldOp;
    private Comparable<T> oldValue;
    private Comparable<T> newValue;

    public PropertyCondition(String thing, String property, String operation, Comparable<T> value, String description) {
        super(thing, description);
        this.name = property;
        this.newOp = operation;
        this.newValue = value;
    }

    public PropertyCondition(String thing, String property, String oldOp, Comparable<T> oldValue,
                             String newOp, Comparable<T> newValue, String description) {
        super(thing, description);
        this.name = property;
        this.oldOp = oldOp;
        this.oldValue = oldValue;
        this.newOp = newOp;
        this.newValue = newValue;
    }

    public String getProperty() {
        return name;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean check(Object data) {

        PropertyUpdatedEventData pd = (PropertyUpdatedEventData) data;
        if(this.oldValue!=null && this.oldOp!=null) {
            return compare(this.oldOp, this.oldValue, (Comparable<T>) pd.getOldValue())
                    && compare(this.newOp, this.newValue, (Comparable<T>) pd.getNewValue());
        } else {
            return compare(this.newOp, this.newValue, (Comparable<T>) pd.getNewValue());
        }
    }

    @SuppressWarnings("unchecked")
    private boolean compare(String op, Comparable<T> a, Comparable<T> b) {
        return (
                (op.equals("==") && a.compareTo((T) b) == 0) ||
                (op.equals(">=") && a.compareTo((T) b) <= 0) ||
                (op.equals(">") && a.compareTo((T) b) < 0) ||
                (op.equals("<=") && a.compareTo((T) b) >= 0) ||
                (op.equals("<") && a.compareTo((T) b) > 0)
        );
    }

    @Override
    public String toString() {
        if(this.oldValue!=null && this.oldOp!=null) {
            return thing+"."+name+" "+"old value "+oldOp+" "+oldValue+" and new value "+newOp+" "+newValue;
        }
        return thing+"."+name+" "+newOp+" "+newValue;
    }
}
