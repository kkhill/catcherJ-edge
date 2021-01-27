package com.kkhill.core.thing.element;

import com.kkhill.core.exception.IllegalThingException;
import com.kkhill.core.thing.Thing;

import java.lang.reflect.Field;

public class Property {

    /** name is unique **/
    private String name;
    private String description;
    private String unitOfMeasurement;
    private Comparable value;
    private Thing thing;
    private Field field;

    public Property(String name, String description, String unitOfMeasurement, Thing thing, Field field) throws IllegalThingException {
        this.name = name;
        this.description = description;
        this.unitOfMeasurement = unitOfMeasurement;
        this.field = field;
        this.thing = thing;
        updateValue();
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

    public Comparable getValue() {
        return this.value;
    }

    @SuppressWarnings("unchecked")
    public Object updateValue() throws IllegalThingException {
        try {
            this.value = (Comparable) field.get(this.thing);
        } catch (IllegalAccessException e) {
            throw new IllegalThingException(String.format(
                    "wrong mapping in property: %s", this.name));
        }
        return this.value;
    }
}
