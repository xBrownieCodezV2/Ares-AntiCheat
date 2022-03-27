package com.gladurbad.ares.bungee;

import com.gladurbad.ares.bungee.dispatcher.DispatcherManager;
import net.md_5.bungee.api.plugin.Plugin;

public class AresBungeePlugin extends Plugin {
    @Override
    public void onEnable() {
        this.getProxy().registerChannel("AresBungee");
        this.getProxy().getPluginManager().registerListener(this, new DispatcherManager());

        AresBungee.INSTANCE.start(this);
    }

    @Override
    public void onDisable() {
        this.getProxy().unregisterChannel("AresBungee");
        AresBungee.INSTANCE.stop(this);
    }
}
