package com.gladurbad.ares.bungee;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public enum AresBungee {

    @Getter(AccessLevel.NONE)
    INSTANCE;

    private AresBungeePlugin plugin;

    public void start(AresBungeePlugin plugin) {
        this.plugin = plugin;
    }

    public void stop(AresBungeePlugin plugin) {
        this.plugin = plugin;
    }
}
