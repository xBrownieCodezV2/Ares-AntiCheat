package com.gladurbad.ares.util.attribute;

import lombok.Getter;

@Getter
public enum AttributeType {

    JUMP(0),
    SPEED(0),
    SPRINT(0),
    SNEAK(0),
    WALK_SPEED(0.1F),
    FLY_SPEED(0.1F),
    ALLOWED_FLYING(0);

    private final double defaultLevel;

    AttributeType(double defaultLevel) {
        this.defaultLevel = defaultLevel;
    }
}
