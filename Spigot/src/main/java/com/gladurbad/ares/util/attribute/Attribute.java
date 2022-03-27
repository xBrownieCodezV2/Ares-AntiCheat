package com.gladurbad.ares.util.attribute;

import cc.ghast.packet.utils.Pair;

public class Attribute extends Pair<Double, Integer> {

    public Attribute(Double level, Integer tick) {
        super(level, tick);
    }

    public Integer getLevel() {
        return super.getK().intValue();
    }

    public Integer getTicks() {
        return super.getV();
    }

    public boolean get() {
        return getLevel().intValue() > 0;
    }

    public void setLevel(double level) {
        super.setK(level);
    }

    public void setTicks(int ticks) {
        super.setV(ticks);
    }

    public Attribute incrementTicks() {
        super.setV(super.getV() + 1);
        return this;
    }

    public boolean passed(int ticks) {
        return getTicks() >= ticks;
    }
}
