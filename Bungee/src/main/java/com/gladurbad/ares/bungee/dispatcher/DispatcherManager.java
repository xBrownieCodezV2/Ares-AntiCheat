package com.gladurbad.ares.bungee.dispatcher;

import com.gladurbad.ares.bungee.dispatcher.alert.AlertDispatcher;
import com.gladurbad.ares.bungee.dispatcher.punishment.PunishmentDispatcher;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashSet;
import java.util.Set;

public class DispatcherManager implements Listener {

    private final Set<Dispatcher> dispatchers;

    public DispatcherManager() {
        dispatchers = new HashSet<>();

        dispatchers.add(new AlertDispatcher());
        dispatchers.add(new PunishmentDispatcher());
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getTag().equals("AresBungee")) {
            return;
        }

        if (event.getSender() instanceof Server) {
            Server sender = (Server) event.getSender();

            ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());

            String channel = input.readUTF();

            dispatchers.forEach(dispatcher -> {
                if (dispatcher.getListenedChannel().equals(channel)) {
                    ProxyServer.getInstance().getServers().forEach((s, serverInfo) -> {
                        if (!s.equals(sender.getInfo().getName())) serverInfo.sendData("AresBungee", dispatcher.dispatch(input).toByteArray());
                    });
                }
            });
        }
    }
}
