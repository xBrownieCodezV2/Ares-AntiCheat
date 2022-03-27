package com.gladurbad.ares.data.setback;

public interface SetbackHandler {
    void tick();

    void setSetback(boolean value);

    void setPosition();

    long elapsedLastSetback();
}
