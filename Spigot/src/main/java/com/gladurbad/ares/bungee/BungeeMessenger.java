package com.gladurbad.ares.bungee;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.AresPlugin;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

@AllArgsConstructor
public class BungeeMessenger implements PluginMessageListener {

    private final AresPlugin plugin;

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("AresBungee")) {
            ByteArrayDataInput input = ByteStreams.newDataInput(message);


            // Read incoming alert and punishments from bungee which were sent from the other servers on the proxy.
            switch (input.readUTF()) {
                case "alert":
                    Ares.INSTANCE.getPlayerDataManager().handleProxyAlert(input);
                    break;
                case "punish":
                    Ares.INSTANCE.getPlayerDataManager().handleProxyPunishment(input);
                    break;
            }
        }
    }


}
