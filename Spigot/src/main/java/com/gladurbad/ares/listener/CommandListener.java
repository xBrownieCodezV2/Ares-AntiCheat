package com.gladurbad.ares.listener;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            PlayerData data = Ares.INSTANCE.getPlayerDataManager().get((Player) sender);

            if (data != null) {
                data.getCommandManager().handle(args);
            }
        }

        return true;
    }
}
