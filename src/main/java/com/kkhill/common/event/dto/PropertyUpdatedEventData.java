package com.kkhill.common.event.dto;

public class PropertyUpdatedEventData {
    private String id;
    private String property;
    private Object oldValue;
    private Object newValue;

    public PropertyUpdatedEventData(String id, String property, Object oldValue, Object newValue) {
        this.id = id;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getId() {
        return id;
    }

    public String getProperty() {
        return property;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }
}
