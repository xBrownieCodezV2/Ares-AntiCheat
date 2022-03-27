package com.gladurbad.ares.data.tracker;

import com.gladurbad.ares.data.PlayerData;

import java.util.List;

public abstract class Tracker {
    public final PlayerData data;

    public Tracker(PlayerData data) {
        this.data = data;
    }

    public <T> List<T> getChecks(Class<T> type) {
        return data.getCheckData().getCategorizedChecks().get(type);
    }
}
