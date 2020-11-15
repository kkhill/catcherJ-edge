package com.kkhill.addons.rulengine.condition;

public abstract class Condition {
    private String conditionType;

    public Condition(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getConditionType() {
        return conditionType;
    }

    public abstract boolean check();
}
