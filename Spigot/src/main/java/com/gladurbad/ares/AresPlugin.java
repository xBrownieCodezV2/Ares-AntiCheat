package com.gladurbad.ares;

import org.bukkit.plugin.java.JavaPlugin;

public final class AresPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Ares.INSTANCE.start(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Ares.INSTANCE.stop(this);
    }
}
