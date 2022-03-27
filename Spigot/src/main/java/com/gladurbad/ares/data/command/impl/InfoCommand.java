package com.gladurbad.ares.data.command.impl;

import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.command.Command;
import com.gladurbad.ares.gui.impl.InfoGUI;

import java.util.List;

public class InfoCommand extends Command {

    public InfoCommand(PlayerData data, String[] args) {
        super(data, args);
    }

    @Override
    public void handle(List<String> args) {
        InfoGUI.INVENTORY.open(data.getPlayer());
    }
}
