package com.kkhill.core.thing;

import java.lang.reflect.Field;

public class Property {

    /** name is unique **/
    private String name;
    private String description;
    private String unitOfMeasurement;
    private Field field;

    public Property(String name, String description, String unitOfMeasurement, Field field) {
        this.name = name;
        this.description = description;
        this.unitOfMeasurement = unitOfMeasurement;
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
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
