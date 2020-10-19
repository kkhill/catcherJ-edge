package com.kkhill.core.thing;

import com.kkhill.common.convention.StateName;
import com.kkhill.core.exception.IllegalThingException;

import java.lang.reflect.Field;

public class State {

    private String description;
    private String value;
    private Thing thing;
    private Field field;


    public State(String description, Thing thing, Field field) throws IllegalThingException {
        this.description = description;
        this.field = field;
        this.thing = thing;
        updateValue();
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public String updateValue() throws IllegalThingException {
        try {
            this.value = (String)field.get(this.thing);
        } catch (IllegalAccessException e) {
            throw new IllegalThingException("wrong mapping in state");
        }
        if(this.value == null) this.value = StateName.UNKNOWN;
        return this.value;
    }

}
