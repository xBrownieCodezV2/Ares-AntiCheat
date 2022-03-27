package com.gladurbad.ares.data.command.impl;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.command.Command;

import java.util.List;
import java.util.Set;

public class AlertCommand extends Command {

    public AlertCommand(PlayerData data, String[] args) {
        super(data, args);
    }

    @Override
    public void handle(List<String> args) {
        Set<PlayerData> alerts = Ares.INSTANCE.getPlayerDataManager().getAlerts();

        if (!alerts.remove(data)) alerts.add(data);

        boolean toggled = alerts.contains(data);

        message(toggled ? config.getToggledAlertsOn() : config.getToggledAlertsOff());
    }
}
