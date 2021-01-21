package com.kkhill.core.thing;

import com.kkhill.common.event.EventType;
import com.kkhill.core.event.Event;
import com.kkhill.core.event.EventBus;
import com.kkhill.core.exception.*;
import com.kkhill.common.event.dto.PropertyUpdatedEventData;
import com.kkhill.common.event.dto.ServiceCalledEventData;
import com.kkhill.common.event.dto.StateUpdatedEventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
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

        String id = thing.build();
        things.put(id, thing);
        EventBus.getInstance().fire(new Event(EventType.THING, "registered", id));
        logger.info("thing registered, name: {}, id: {}", thing.getFriendlyName(), id);
        return id;
    }

    public void removeThing(String thingId) {

        things.remove(thingId);
        EventBus.getInstance().fire(new Event(EventType.THING, "removed", thingId));
        logger.info("thing removed, id: {}", thingId);
    }

    public Map<String, Thing> getThings() {
        return this.things;
    }

    public Map<String, Thing> getThings(String type) {
        Map<String, Thing> res = new HashMap<>();
        for(Thing thing : this.things.values()) {
            if(thing.getType().equals(type)) res.put(thing.getId(), thing);
        }
        return res;
    }

    public Thing getThing(String id) throws NotFoundException {
        Thing thing = this.things.get(id);
        if(thing == null) throw new NotFoundException(String.format("not found thing: %s", id));
        return thing;
    }

    public String[] updateState(String id) throws NotFoundException, IllegalThingException {

        State state = getThing(id).getState();
        String oldState = state.getValue();
        String newState = state.updateValue();
        logger.debug("update thing state, id: {}, from {} to {}", id, oldState, newState);
        return new String[]{oldState, newState};
    }

    public String[] updateStateAndNotify(String id) throws NotFoundException, IllegalThingException {

        String[] s = updateState(id);
        // do not notify if state is not changed
        if(!s[0].equals(s[1])) {
            Object data = new StateUpdatedEventData(id, s[0], s[1]);
            EventBus.getInstance().fire(new Event(EventType.STATE_UPDATED, id, data));
        }
        return s;
    }


    public Object[] updateProperty(String id, String name) throws NotFoundException, IllegalThingException {

        Property property = getThing(id).getProperties().get(name);
        if(property == null) throw new NotFoundException(String.format("property not found: %s", name));
        Object oldValue = property.getValue();
        Object newValue = property.updateValue();
        logger.debug("update thing property, id: {}, property: {}, from {} to {}", id, name, oldValue, newValue);
        return new Object[]{oldValue, newValue};
    }

    public Object[] updatePropertyAndNotify(String id, String name) throws NotFoundException, IllegalThingException {

        Object[] o = updateProperty(id, name);
        if(!o[0].equals(o[1])) {
            Object data = new PropertyUpdatedEventData(id, name, o[0], o[1]);
            EventBus.getInstance().fire(new Event(EventType.PROPERTY_UPDATED, id, data));
        }
        return o;
    }

    public Object callService(String thingId, String service, Map<String, Object> args) throws NotFoundException {

        Service s = getThing(thingId).getServices().get(service);
        if (s == null) throw new NotFoundException(String.format("service not found: %s", service));
        Object res = null;
        try {
            res = s.call(args);
            logger.debug("call thing service, id: {}, service: {}", thingId, service);
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.error("error with calling thing service");
            e.printStackTrace();
        }
        return res;
    }

    public Object callServiceAndNotify(String thingId, String service, Map<String, Object> args) throws NotFoundException {

        Object res = callService(thingId, service, args);
        Object data = new ServiceCalledEventData(thingId, service);
        EventBus.getInstance().fire(new Event(EventType.SERVICE_CALLED, thingId, data));
        return res;
    }

    public void enableThing(String thingId) throws NotFoundException {

        Thing thing = getThing(thingId);
        if(!thing.isAvailable()) {
            thing.enable();
            EventBus.getInstance().fire(new Event(EventType.THING, thingId, "enabled"));
            logger.debug("enable thing, id: {}", thingId);
        }
    }

    public void disableThing(String thingId) throws NotFoundException {

        Thing thing = getThing(thingId);
        if(thing.isAvailable()) {
            thing.disable();
            EventBus.getInstance().fire(new Event(EventType.THING, thingId, "disabled"));
            logger.debug("disable thing, id: {}", thingId);
        }
    }
}
