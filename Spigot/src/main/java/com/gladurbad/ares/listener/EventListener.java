package com.gladurbad.ares.listener;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.nms.NmsUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.nio.charset.StandardCharsets;

public class EventListener implements PluginMessageListener, Listener {

    public EventListener() {
        Bukkit.getPluginManager().registerEvents(this, Ares.INSTANCE.getPlugin());
        Bukkit.getMessenger().registerIncomingPluginChannel(Ares.INSTANCE.getPlugin(), "MC|Brand", this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Ares.INSTANCE.getPlayerDataManager().add(event.getPlayer());

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Ares.INSTANCE.getPlayerDataManager().remove(event.getPlayer());
        NmsUtil.addChannel(event.getPlayer(), "MC|Brand");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("MC|Brand")) {
            final PlayerData data = Ares.INSTANCE.getPlayerDataManager().get(player);
            if (data == null) return;
            data.setClientBrand(new String(message, StandardCharsets.UTF_8).substring(1));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAttack(EntityDamageByEntityEvent event) {
        event.setCancelled(false);
    }
}
