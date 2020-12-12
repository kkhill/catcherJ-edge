package com.kkhill.core.event.dto;

public class PropertyUpdatedEventData {
    private String thingId;
    private String property;
    private Object oldValue;
    private Object newValue;

    public PropertyUpdatedEventData(String thingId, String property, Object oldValue, Object newValue) {
        this.thingId = thingId;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getThingId() {
        return thingId;
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
