package com.kkhill.core.event;

import java.util.Date;

public class Event {

    private String type;
    private String topic;
    private Object data;
    private long timestamp;

    public Event(String type, String topic, Object data) {
        this.type = type;
        this.topic = topic;
        this.data = data;
        this.timestamp = new Date().getTime();
    }

    public String getType() {
        return type;
    }

    public String getTopic() {
        return topic;
    }

    public Object getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
