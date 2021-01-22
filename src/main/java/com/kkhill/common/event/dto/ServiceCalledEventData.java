package com.kkhill.common.event.dto;

public class ServiceCalledEventData {
    private String id;
    private String service;

    public ServiceCalledEventData(String id, String service) {
        this.id = id;
        this.service = service;
    }

    public String getId() {
        return id;
    }

    public String getService() {
        return service;
    }
}
