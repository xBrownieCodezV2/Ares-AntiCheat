package com.gladurbad.ares.data.command.impl;

import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.command.Command;
import com.gladurbad.ares.gui.impl.MenuGUI;

import java.util.List;

public class GUICommand extends Command {

    public GUICommand(PlayerData data, String[] args) {
        super(data, args);
    }

    @Override
    public void handle(List<String> args) {
        MenuGUI.INVENTORY.open(data.getPlayer());
    }
}
