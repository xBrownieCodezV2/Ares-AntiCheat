package com.gladurbad.ares.data.command;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.check.AbstractCheck;
import com.gladurbad.ares.config.ConfigManager;
import com.gladurbad.ares.data.PlayerData;
import org.bukkit.Bukkit;

import java.util.List;

public abstract class Command {

    protected final PlayerData data;
    protected final ConfigManager config = Ares.INSTANCE.getConfigManager();
    private final String[] args;

    public Command(PlayerData data, String[] args) {
        this.data = data;
        this.args = args;
    }

    public abstract void handle(List<String> args);

    public PlayerData findData(String name) {
        return Ares.INSTANCE.getPlayerDataManager().get(Bukkit.getPlayer(name));
    }

    public AbstractCheck findCheck(Class<? extends AbstractCheck> checkClass) {
        return findCheck(checkClass, data);
    }

    public AbstractCheck findCheck(Class<? extends AbstractCheck> checkClass, PlayerData data) {
        return data.getCheckData().getCheck(checkClass);
    }

    public void message(String... message) {
        data.message(message);
    }

    public void message(List<String> messages) {
        messages.forEach(data::message);
    }

    public String[] getArguments() {
        return args;
    }
}
