package com.kkhill.addons.rulengine.helper;

public class PropertyCondition extends Condition {

    private String thing;
    private String property;
    private String operation;
    private Object value;

    public PropertyCondition(String thing, String property, String operation, Object value) {
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
}
