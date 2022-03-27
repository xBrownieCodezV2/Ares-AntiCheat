package com.gladurbad.ares.util.tick;

import lombok.Getter;

public class TickTimer {

    @Getter
    private int ticks;

    public void tick() {
        ticks++;
    }

    public void reset() {
        ticks = 0;
    }

    public boolean passed(int ticks) {
        return this.ticks > ticks;
    }

    public boolean occurred(int ticks) {
        return this.ticks <= ticks;
    }
}
