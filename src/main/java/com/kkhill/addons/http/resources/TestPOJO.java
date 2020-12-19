package com.kkhill.addons.http.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TestPOJO {
    private String id;

    public TestPOJO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
