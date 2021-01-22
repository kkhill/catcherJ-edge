package com.kkhill.common.event.dto;

public class StateUpdatedEventData {
    private String id;
    private String oldState;
    private String newState;

    public StateUpdatedEventData(String id, String oldState, String newState) {
        this.id = id;
        this.oldState = oldState;
        this.newState = newState;
    }

    public String getId() {
        return id;
    }

    public String getOldState() {
        return oldState;
    }

    public String getNewState() {
        return newState;
    }
}
