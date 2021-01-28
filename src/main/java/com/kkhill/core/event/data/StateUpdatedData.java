package com.kkhill.core.event.data;

public class StateUpdatedData {
    private String id;
    private String oldState;
    private String newState;

    public StateUpdatedData(String id, String oldState, String newState) {
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
