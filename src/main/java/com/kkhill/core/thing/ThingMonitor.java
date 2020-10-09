package com.kkhill.core.thing;

import com.kkhill.common.convention.EventType;
import com.kkhill.core.event.Event;
import com.kkhill.core.event.EventBus;
import com.kkhill.core.thing.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ThingMonitor {

    private final Logger logger = LoggerFactory.getLogger(ThingMonitor.class);

    private Map<String, Thing> things;

    private ThingMonitor() {
        this.things = new ConcurrentHashMap<>();
    }

    private static class Holder {
        private static ThingMonitor instance = new ThingMonitor();
    }

    public static ThingMonitor getInstance() {
        return Holder.instance;
    }

    /**
     * parse state, properties, services in a thing and register
     *
     * @param thing
     * @return
     * @throws IllegalThingException
     */
    public String registerThing(Thing thing) throws IllegalThingException {

        String id = UUID.randomUUID().toString().replace("-", "");
        thing.setID(id);
        things.put(id, thing);

        // extract state and properties of thing
        Field[] fields = thing.getClass().getDeclaredFields();
        int stateNum = 0;
        for(Field field : fields) {
            field.setAccessible(true);
            com.kkhill.core.thing.annotation.State s = field.getAnnotation(com.kkhill.core.thing.annotation.State.class);
            com.kkhill.core.thing.annotation.Property p = field.getAnnotation(com.kkhill.core.thing.annotation.Property.class);
            if(s != null) {
                stateNum ++;
                if(stateNum > 1) throw new NotSingleStateException("a thing must have only one state field");
                State state = new State(s.description(), field);
                thing.setState(state);
            }
            if(p != null) {
                Property property = new Property(p.name(), p.description(), p.unitOfMeasurement(), field);
                thing.addProperty(property);
            }
        }
        if(stateNum == 0) throw new NotSingleStateException("a thing must have one state field");

        // extract services of thing
        Method[] methods = thing.getClass().getDeclaredMethods();
        for(Method method : methods) {
            method.setAccessible(true);
            com.kkhill.core.thing.annotation.Service s = method.getAnnotation(com.kkhill.core.thing.annotation.Service.class);
            if(s != null) {
                Service service = new Service(s.name(), s.description(), thing, method);
                thing.addService(service);
            }
        }

        EventBus.getInstance().fire(new Event(EventType.THING, "registered", id));
        logger.info("thing has been registered, id: {}", id);

        return id;
    }

    public void removeThing(String thingID) {

        things.remove(thingID);
        EventBus.getInstance().fire(new Event(EventType.THING, "removed", thingID));
        logger.info("thing has been removed, id: {}", thingID);
    }

    public Map<String, Thing> getThings() {
        return this.things;
    }

    public Thing getThing(String thingID) throws ThingNotFoundException {
        Thing thing = this.things.get(thingID);
        if(thing == null) throw new ThingNotFoundException();
        return thing;
    }

    /**
     * update and notify state of a thing
     * usually used by thing implementation
     * e.g. updateAndNotifyState(this, "on");
     *
     * @param id
     * @param value
     * @throws ThingNotFoundException
     * @throws IllegalAccessException
     */
    public void updateAndNotifyState(String id, Object value) throws ThingNotFoundException, IllegalAccessException {

        Thing thing = getThing(id);
        Map<String, Object> data = new HashMap<>();
        data.put("thingID", thing.getID());
        data.put("old_state", thing.getState().getValue(thing));
        data.put("new_state", value);
        thing.updateState(value);
        EventBus.getInstance().fire(new Event(EventType.STATE_UPDATED, thing.getID(), data));
        logger.info("update thing state, id: {}, from {} to {}",
                thing.getID(), data.get("old_state"), data.get("new_state"));
    }

    /**
     * update property and fire event bus
     * usually used by thing implementation
     * e.g. updateAndNotifyProperty(this, "brightness", 50)
     *
     * @param id
     * @param name
     * @param value
     * @throws ThingNotFoundException
     * @throws PropertyNotFoundException
     * @throws IllegalAccessException
     */
    public void updateAndNotifyProperty(String id, String name, Object value) throws ThingNotFoundException, PropertyNotFoundException, IllegalAccessException {

        Thing thing = getThing(id);
        Property property = thing.getProperties().get(name);
        if(property == null) throw new PropertyNotFoundException();
        if(property.getValue(thing).equals(value)) return;
        Map<String, Object> data = new HashMap<>();
        data.put("thingID", thing.getID());
        data.put("property", name);
        data.put("old_value", property.getValue(thing));
        data.put("new_value", value);
        thing.updateProperty(name, value);
        EventBus.getInstance().fire(new Event(EventType.PROPERTY_UPDATED, thing.getID(), data));
        logger.info("update thing property, id: {}, from {} to {}",
                thing.getID(), data.get("old_value"), data.get("new_value"));

    }

    /**
     * call service of thing
     * usually used by other things rather than this.
     * e.g. callService("some_id_from_UI", "set_brightness", new Object[]{50})
     *
     *
     * @param thingID
     * @param service
     * @param args
     * @throws ThingNotFoundException
     * @throws ServiceNotFoundException
     */
    public void callService(String thingID, String service, Object[] args) throws ThingNotFoundException, ServiceNotFoundException {

        Service s = getThing(thingID).getServices().get(service);
        if (s == null) throw new ServiceNotFoundException();
        Map<String, Object> data = new HashMap<>();
        data.put("thingID", thingID);
        data.put("service", service);
        try {
            s.invoke(args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        EventBus.getInstance().fire(new Event(EventType.SERVICE_UPDATED, thingID, data));
        logger.info("call thing service, id: {}, service: {}", thingID, service);
    }

    public void enableThing(String thingID) throws ThingNotFoundException {

        Thing thing = getThing(thingID);
        if(!thing.isAvailable()) {
            thing.enable();
            EventBus.getInstance().fire(new Event(EventType.THING, thingID, "enabled"));
            logger.info("enable thing, id: {}", thingID);
        }
    }

    public void disableThing(String thingID) throws ThingNotFoundException {

        Thing thing = getThing(thingID);
        if(thing.isAvailable()) {
            thing.disable();
            EventBus.getInstance().fire(new Event(EventType.THING, thingID, "disabled"));
            logger.info("disable thing, id: {}", thingID);
        }
    }
}
