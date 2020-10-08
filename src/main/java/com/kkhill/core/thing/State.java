package com.kkhill.core.thing;

import java.lang.reflect.Field;

public class State {

    String description;
    Field field;

    public State(String description, Field field) {

        this.description = description;
        this.field = field;
    }

    public String getDescription() {
        return description;
    }

    public Object getValue(Thing thing) throws IllegalAccessException {
        return field.get(thing);
    }

    public void setValue(Thing thing, Object obj) throws IllegalAccessException {
        field.set(thing, obj);
    }

}
