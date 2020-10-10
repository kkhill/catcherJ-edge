package com.kkhill.core.util;

import com.kkhill.core.Catcher;
import com.kkhill.core.thing.Property;
import com.kkhill.core.thing.Service;
import com.kkhill.core.thing.State;
import com.kkhill.core.thing.Thing;
import com.kkhill.core.exception.IllegalThingException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ThingUtil {

    /**
     * parse annotations in a thing, and build it
     * @param thing
     */
    public static void buildThing(Thing thing) throws IllegalThingException {

        // extract state and properties of thing
        Field[] fields = thing.getClass().getDeclaredFields();
        int stateNum = 0;
        for(Field field : fields) {
            field.setAccessible(true);
            com.kkhill.core.annotation.State s = field.getAnnotation(com.kkhill.core.annotation.State.class);
            com.kkhill.core.annotation.Property p = field.getAnnotation(com.kkhill.core.annotation.Property.class);
            // build state
            if(s != null) {
                stateNum ++;
                if(stateNum > 1) throw new IllegalThingException("a thing must have only one state field");
                State state = new State(s.description(), thing, field);
                thing.setState(state);
            }
            // build properties
            if(p != null) {
                Property property = new Property(p.name(), p.description(), p.unitOfMeasurement(), thing, field);
                thing.addProperty(property);
            }
        }
        if(stateNum == 0) throw new IllegalThingException("a thing must have a state field");

        // extract services and polling method of a thing
        Method[] methods = thing.getClass().getDeclaredMethods();
        for(Method method : methods) {
            method.setAccessible(true);
            com.kkhill.core.annotation.Service s = method.getAnnotation(com.kkhill.core.annotation.Service.class);
            // build service
            if(s != null) {
                Service service = new Service(s.name(), s.description(), thing, method);
                if(s.poll()) {
                    service.enablePolling();
                    // add to scheduler
                    Catcher.getScheduler().addPolledService(service);
                }
                thing.addService(service);
            }
        }
    }
}
