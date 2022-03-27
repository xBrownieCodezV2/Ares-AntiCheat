package com.gladurbad.ares.data.command.impl;

import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.command.Command;
import com.gladurbad.ares.gui.impl.PlayerInfoGUI;

import java.util.List;

public class PlayerInfoCommand extends Command {

    public PlayerInfoCommand(PlayerData data, String[] args) {
        super(data, args);
    }

    @Override
    public void handle(List<String> args) {
        PlayerData subject = findData(args.get(0));

        if (subject != null) {
            PlayerInfoGUI.getInventory(subject.getPlayer()).open(data.getPlayer());
        }
    }
}
