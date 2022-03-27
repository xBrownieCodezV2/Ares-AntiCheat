package com.gladurbad.ares.data.command.impl;

import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.command.Command;
import com.gladurbad.ares.gui.impl.ChecksGUI;

import java.util.List;

public class ChecksCommand extends Command {

    public ChecksCommand(PlayerData data, String[] args) {
        super(data, args);
    }

    @Override
    public void handle(List<String> args) {
        ChecksGUI.INVENTORY.open(data.getPlayer());
    }
}
