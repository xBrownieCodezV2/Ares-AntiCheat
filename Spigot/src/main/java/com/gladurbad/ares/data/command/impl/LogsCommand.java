package com.gladurbad.ares.data.command.impl;

import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.command.Command;
import com.gladurbad.ares.gui.impl.LogsGUI;

import java.util.List;

public class LogsCommand extends Command {

    public LogsCommand(PlayerData data, String[] args) {
        super(data, args);
    }

    @Override
    public void handle(List<String> args) {
        PlayerData subject = findData(args.get(0));

        if (subject != null) {
            if (subject.getCheckData().getTotalVl() > 0) {
                LogsGUI.getInventory(subject.getPlayer()).open(data.getPlayer());
            } else {
                message("&cThis player has no violations!");
            }
        } else {
            message("&cCould not find player " + args.get(0));
        }
    }
}
