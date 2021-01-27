package com.kkhill.addons.automation.trigger;

import com.kkhill.addons.automation.utils.RuleUtil;
import com.kkhill.common.event.EventType;

public class PropertyUpdatedTrigger extends Trigger {

    private String property;
    private String oldOp;
    private Comparable oldValue;
    private String newOp;
    private Comparable newValue;


    public PropertyUpdatedTrigger(String thing, String description,String property,
                                  String oldOp, Comparable oldValue,
                                  String newOp, Comparable newValue) {
        super(EventType.PROPERTY_UPDATED, thing, description);
        this.property = property;
        this.oldOp = oldOp;
        this.oldValue = oldValue;
        this.newOp = newOp;
        this.newValue = newValue;
    }

    public String getProperty() {
        return property;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean check(String thing, Object from, Object to) {
        return this.thing.equals(thing) &&
                RuleUtil.compare(this.oldOp, this.oldValue, (Comparable) from) &&
                RuleUtil.compare(this.newOp, this.newValue, (Comparable) to);
    }

    @Override
    public String toString() {
        return "PropertyUpdatedTrigger{" +
                "property='" + property + '\'' +
                ", oldOp='" + oldOp + '\'' +
                ", oldValue=" + oldValue +
                ", newOp='" + newOp + '\'' +
                ", newValue=" + newValue +
                '}';
    }
}
