package com.kkhill.core.event.dto;

public class ServiceCalledEventData {
    private String thingId;
    private String service;

    public ServiceCalledEventData(String thingId, String service) {
        this.thingId = thingId;
        this.service = service;
    }

    public String getThingId() {
        return thingId;
    }

    public String getService() {
        return service;
    }
}
