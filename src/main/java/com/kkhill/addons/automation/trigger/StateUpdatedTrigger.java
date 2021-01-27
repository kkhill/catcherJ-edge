package com.kkhill.addons.automation.trigger;

public class StateUpdatedTrigger extends Trigger {

    private String from;
    private String to;

    public StateUpdatedTrigger(String thing, String description, String from, String to) {
        super("stateUpdated", thing, description);
        this.from = from;
        this.to = to;
    }


    @Override
    public boolean check(String thing, Object from, Object to) {
        return this.thing.equals(thing) && this.from.equals(from) && this.to.equals(to);
    }

    @Override
    public String toString() {
        return "StateUpdatedTrigger{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
