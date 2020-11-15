package com.kkhill.addons.rulengine.action;

public abstract class Action {
    private String actionType;

    public Action(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }
}
