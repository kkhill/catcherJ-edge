package com.kkhill.core.thing;

import com.kkhill.core.event.Event;
import com.kkhill.core.event.EventBus;
import com.kkhill.core.event.EventType;
import com.kkhill.core.exception.PropertyNotFound;
import com.kkhill.core.exception.ServiceNotFound;
import com.kkhill.core.exception.ThingNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void removeThing(String thingID){

        things.remove(thingID);
        EventBus.getInstance().fire(new Event(EventType.THING, "removed", thingID));
        logger.info("thing has been removed, id: {}", thingID);
    }

    public void registerThing(Thing thing) {

        String id = UUID.randomUUID().toString().replace("-", "");
        thing.setID(id);
        things.put(id, thing);
        EventBus.getInstance().fire(new Event(EventType.THING, "registered", id));
        logger.info("thing has been registered, id: {}", id);
    }

    public Map<String, Thing> getThings() {
        return this.things;
    }

    public Thing getThing(String thingID) throws ThingNotFound {
        Thing thing = this.things.get(thingID);
        if(thing == null) throw new ThingNotFound();
        return thing;
    }

    public void updateState(String thingID, State state) throws ThingNotFound {

        Thing thing = getThing(thingID);
        if(thing.getState().getName() == state.getName()) return;
        Map<String, String> data = new HashMap<>();
        data.put("thingID", thing.getID());
        data.put("old_state", thing.getState().getName());
        data.put("new_state", state.getName());
        thing.setState(state);
        EventBus.getInstance().fire(new Event(EventType.STATE_UPDATED, thingID, data));
        logger.info("update thing state, id: {}, from {} to {}", thingID, thing.getState().getName(), state.getName());
    }

    public void updateProperty(String thingID, Property property) throws ThingNotFound, PropertyNotFound {

        Thing thing = getThing(thingID);
        Property p = thing.getProperties().get(property.getName());
        if(p == null) throw new PropertyNotFound();
        if(p.getValue().equals(property.getValue())) return;
        Map<String, Object> data = new HashMap<>();
        data.put("thingID", thing.getID());
        data.put("property", p.getName());
        data.put("old_value", p.getValue());
        data.put("new_value", property.getValue());
        thing.setProperty(property);
        EventBus.getInstance().fire(new Event(EventType.PROPERTY_UPDATED, thingID, data));
        logger.info("update thing property, id: {}, from {} to {}", thingID, p.getValue(), property.getValue());
    }


    public void callService(String thingID, String service, Object args) throws ThingNotFound, ServiceNotFound {

        Thing thing = getThing(thingID);
        Service s = thing.getServices().get(service);
        if (s == null) throw new ServiceNotFound();
        Map<String, Object> data = new HashMap<>();
        data.put("thingID", thing.getID());
        data.put("service", service);
        s.invoke();
        EventBus.getInstance().fire(new Event(EventType.SERVICE_UPDATED, thingID, data));
        logger.info("call thing service, id: {}, service: {}", thingID, service);
    }

    public void enableThing(String thingID) throws ThingNotFound {

        Thing thing = getThing(thingID);
        if(!thing.isAvailable()) {
            thing.enable();
            EventBus.getInstance().fire(new Event(EventType.THING, thingID, "enabled"));
            logger.info("enable thing, id: {}", thingID);
        }
    }

    public void disableThing(String thingID) throws ThingNotFound {

        Thing thing = getThing(thingID);
        if(thing.isAvailable()) {
            thing.disable();
            EventBus.getInstance().fire(new Event(EventType.THING, thingID, "disabled"));
            logger.info("disable thing, id: {}", thingID);
        }
    }
}
