package com.kkhill.core.thing;

import com.kkhill.common.convention.EventType;
import com.kkhill.core.event.Event;
import com.kkhill.core.event.EventBus;
import com.kkhill.core.exception.*;
import com.kkhill.core.util.ThingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
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
    public String registerThing(Thing thing) throws IllegalThingException, IllegalThingException {

        String id = UUID.randomUUID().toString().replace("-", "");
        thing.setID(id);
        things.put(id, thing);
        ThingUtil.buildThing(thing);
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
     * @throws ThingNotFoundException
     * @throws IllegalThingException
     */
    public void updateAndNotifyState(String id) throws ThingNotFoundException, IllegalThingException {

        State state = getThing(id).getState();
        String oldState = state.getValue();
        state.updateValue();
        String newState = state.getValue();
        // do not notify if state is not changed
        if(oldState.equals(newState)) return;

        Map<String, Object> data = new HashMap<>();
        data.put("thingID", id);
        data.put("old_state", oldState);
        data.put("new_state", newState);
        EventBus.getInstance().fire(new Event(EventType.STATE_UPDATED, id, data));
        logger.info("update thing state, id: {}, from {} to {}", id, oldState, newState);
    }

    /**
     * update property and fire event bus
     * usually used by thing implementation
     * e.g. updateAndNotifyProperty(this, "brightness", 50)
     *
     * @param id
     * @param name
     * @throws ThingNotFoundException
     * @throws PropertyNotFoundException
     * @throws IllegalAccessException
     */
    public void updateAndNotifyProperty(String id, String name) throws ThingNotFoundException, PropertyNotFoundException, IllegalThingException {

        Property property = getThing(id).getProperties().get(name);
        if(property == null) throw new PropertyNotFoundException();

        Object oldValue = property.getValue();
        property.updateValue();
        Object newValue = property.getValue();
        // do not notify if value is not changed
        if(oldValue.equals(newValue)) return;

        Map<String, Object> data = new HashMap<>();
        data.put("thingID", id);
        data.put("property", name);
        data.put("old_value", oldValue);
        data.put("new_value", newValue);
        EventBus.getInstance().fire(new Event(EventType.PROPERTY_UPDATED, id, data));
        logger.info("update thing property, id: {}, from {} to {}",
                id, oldValue, newValue);

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
