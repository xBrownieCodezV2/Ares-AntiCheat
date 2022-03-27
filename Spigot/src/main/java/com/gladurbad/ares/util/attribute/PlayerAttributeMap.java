package com.gladurbad.ares.util.attribute;

import java.util.HashMap;
import java.util.Map;

public class PlayerAttributeMap {

    private final Map<AttributeType, Attribute> attributeMap = new HashMap<>();

    public PlayerAttributeMap() {
        for (AttributeType type : AttributeType.values()) {
            attributeMap.put(type, new Attribute(type.getDefaultLevel(), 0));
        }
    }

    private void set(AttributeType type, int tick) {
        attributeMap.get(type).setTicks(tick);
    }

    public Attribute get(AttributeType type) {
        return attributeMap.get(type);
    }

    public void reset(AttributeType type) {
        set(type, 0);
    }

    public void tick() {
        attributeMap.values().forEach(Attribute::incrementTicks);
    }
}
