package com.kkhill.addon.rulengine.helper;

public class StateCondition extends Condition{

    private String thing;
    private String from;
    private String to;
    private String on;

    public StateCondition(String thing, String from, String to) {
        this.thing = thing;
        this.from = from;
        this.to = to;
    }

    public StateCondition(String thing, String on) {
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
}
