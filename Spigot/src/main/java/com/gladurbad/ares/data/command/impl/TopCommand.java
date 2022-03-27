package com.gladurbad.ares.data.command.impl;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.command.Command;
import com.gladurbad.ares.gui.impl.TopViolationGUI;

import java.util.List;

public class TopCommand extends Command {

    public TopCommand(PlayerData data, String[] args) {
        super(data, args);
    }

    @Override
    public void handle(List<String> args) {
        if (Ares.INSTANCE.getPlayerDataManager().getData().stream().anyMatch(data -> data.getCheckData().getTotalVl() > 0)) {
            TopViolationGUI.INVENTORY.open(data.getPlayer());
        } else {
            message("&cThere are no violating players!");
        }
    }
}
