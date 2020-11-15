package com.kkhill.utils.event;

public class StateUpdatedEventData {
    private String thingId;
    private String oldState;
    private String newState;

    public StateUpdatedEventData(String thingId, String oldState, String newState) {
        this.thingId = thingId;
        this.oldState = oldState;
        this.newState = newState;
    }

    public String getThingId() {
        return thingId;
    }

    public String getOldState() {
        return oldState;
    }

    public String getNewState() {
        return newState;
    }
}
