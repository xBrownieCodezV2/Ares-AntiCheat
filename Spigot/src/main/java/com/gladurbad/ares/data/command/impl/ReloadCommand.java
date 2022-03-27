package com.gladurbad.ares.data.command.impl;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.command.Command;
import org.bukkit.Bukkit;

import java.util.List;

public class ReloadCommand extends Command {

    public ReloadCommand(PlayerData data, String[] args) {
        super(data, args);
    }

    @Override
    public void handle(List<String> args) {
        Ares.INSTANCE.getPlugin().reloadConfig();
        Ares.INSTANCE.getConfigManager().load();

        Bukkit.getOnlinePlayers().forEach(player -> {
            Ares.INSTANCE.getPlayerDataManager().remove(player);
            Ares.INSTANCE.getPlayerDataManager().add(player);
        });

        message("&cReloaded Ares.");
    }
}
