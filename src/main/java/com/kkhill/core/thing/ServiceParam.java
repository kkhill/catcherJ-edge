package com.kkhill.core.thing;

public class ServiceParam {

    /** parameter name will be used to reorder, to satisfied method.invoke(args) **/
    private String name;
    /** parameter type will be used to deserialize byte[] **/
    private String type;
    private String description;

    public ServiceParam(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
