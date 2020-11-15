package com.kkhill.addons.rulengine.condition;

public class StateCondition extends Condition{

    private String thing;
    private String from;
    private String to;
    private String on;

    public StateCondition(String conditionType, String thing, String from, String to) {
        super(conditionType);
        this.thing = thing;
        this.from = from;
        this.to = to;
    }

    public StateCondition(String conditionType, String thing, String on) {
        super(conditionType);
        this.thing = thing;
        this.on = on;
    }

    public String getThing() {
        return thing;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getOn() {
        return on;
    }

    @Override
    public boolean check() {
        return false;
    }
}
