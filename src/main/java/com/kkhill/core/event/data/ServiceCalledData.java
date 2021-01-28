package com.kkhill.core.event.data;

public class ServiceCalledData {
    private String id;
    private String service;

    public ServiceCalledData(String id, String service) {
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
