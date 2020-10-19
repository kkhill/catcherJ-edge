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
        thing.setId(id);
        things.put(id, thing);
        ThingUtil.buildThing(thing);
        EventBus.getInstance().fire(new Event(EventType.THING, "registered", id));
        logger.info("thing has been registered, id: {}", id);

        return id;
    }

    public void removeThing(String thingId) {

        things.remove(thingId);
        EventBus.getInstance().fire(new Event(EventType.THING, "removed", thingId));
        logger.info("thing has been removed, id: {}", thingId);
    }

    public Map<String, Thing> getThings() {
        return this.things;
    }

    public Thing getThing(String id) throws NotFoundException {
        Thing thing = this.things.get(id);
        if(thing == null) throw new NotFoundException(String.format("not found thing: %s", id));
        return thing;
    }

    /**
     * update and notify state of a thing
     * do not fire event bus if not changed
     * usually used by thing implementation
     * e.g. updateAndNotifyState(this, "on");
     *
     * @param id
     * @throws NotFoundException
     * @throws IllegalThingException
     */
    public void updateAndNotifyState(String id) throws NotFoundException, IllegalThingException {

        State state = getThing(id).getState();
        String oldState = state.getValue();
        String newState = state.updateValue();
        // do not notify if state is not changed
        if(oldState.equals(newState)) return;

        Map<String, Object> data = new HashMap<>();
        data.put("thingId", id);
        data.put("old_state", oldState);
        data.put("new_state", newState);
        EventBus.getInstance().fire(new Event(EventType.STATE_UPDATED, id, data));
        logger.info("update thing state, id: {}, from {} to {}", id, oldState, newState);
    }

    /**
     * update property and notify
     * do not fire event bus if not changed
     * usually used by thing implementation
     * e.g. updateAndNotifyProperty(this, "brightness", 50)
     *
     * @param id
     * @param name
     * @throws NotFoundException
     * @throws IllegalAccessException
     */
    public void updateAndNotifyProperty(String id, String name) throws NotFoundException, IllegalThingException {

        Property property = getThing(id).getProperties().get(name);
        if(property == null) throw new NotFoundException(String.format("not found property: %s", name));

        Object oldValue = property.getValue();
        Object newValue = property.updateValue();
        // do not notify if value is not changed
        if(oldValue.equals(newValue)) return;

        Map<String, Object> data = new HashMap<>();
        data.put("thingId", id);
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
     * @param thingId
     * @param service
     * @param args
     * @throws NotFoundException
     */
    public Object callService(String thingId, String service, Object... args) throws NotFoundException {

        Service s = getThing(thingId).getServices().get(service);
        if (s == null) throw new NotFoundException(String.format("service not found: %s", service));
        Map<String, Object> data = new HashMap<>();
        data.put("thingId", thingId);
        data.put("service", service);
        Object res = null;
        try {
            res = s.invoke(args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        EventBus.getInstance().fire(new Event(EventType.SERVICE_UPDATED, thingId, data));
        logger.info("call thing service, id: {}, service: {}", thingId, service);
        return res;
    }

    public void enableThing(String thingId) throws NotFoundException {

        Thing thing = getThing(thingId);
        if(!thing.isAvailable()) {
            thing.enable();
            EventBus.getInstance().fire(new Event(EventType.THING, thingId, "enabled"));
            logger.info("enable thing, id: {}", thingId);
        }
    }

    public void disableThing(String thingId) throws NotFoundException {

        Thing thing = getThing(thingId);
        if(thing.isAvailable()) {
            thing.disable();
            EventBus.getInstance().fire(new Event(EventType.THING, thingId, "disabled"));
            logger.info("disable thing, id: {}", thingId);
        }
    }
}
