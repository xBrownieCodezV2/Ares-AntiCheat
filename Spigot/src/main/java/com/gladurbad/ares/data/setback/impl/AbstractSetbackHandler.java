package com.gladurbad.ares.data.setback.impl;

import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.setback.SetbackHandler;

public abstract class AbstractSetbackHandler implements SetbackHandler {
    protected PlayerData data;

    public AbstractSetbackHandler(PlayerData data) {
        this.data = data;
    }
}
