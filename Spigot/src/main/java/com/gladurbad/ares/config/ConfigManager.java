package com.gladurbad.ares.config;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.util.string.StringUtil;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

@Getter
public class ConfigManager {

    private boolean loaded;

    public ConfigManager() {
        loaded = false;
    }

    public void load() {
        loaded = false;

        FileConfiguration config = Ares.INSTANCE.getPlugin().getConfig();

        timeBetweenAlerts = config.getInt("time-between-alerts");
        broadcastPunishment = config.getBoolean("broadcast-punishment");
        transactionResponseTimeout = config.getInt("transaction-response-timeout");
        broadcastPunishmentFormat = StringUtil.color(config.getString("broadcast-punishment-format"));
        alertFormat = StringUtil.color(config.getString("alert-format"));
        alertHoverFormat = StringUtil.color(config.getString("alert-hover-format"));
        toggledAlertsOn = StringUtil.color(config.getString("toggled-alerts-on"));
        toggledAlertsOff = StringUtil.color(config.getString("toggled-alerts-off"));
        infoCommand = StringUtil.color(config.getStringList("info-command"));
        invalidArguments = StringUtil.color(config.getStringList("invalid-arguments"));
        argumentResponseColor = StringUtil.color(config.getString("argument-response-color"));
        commandResponseHeader = StringUtil.color(config.getString("command-response-header"));
        commandResponseColor = StringUtil.color(config.getString("command-response-color"));
        creativeBypass = config.getBoolean("creative-bypass");

        ConfigurationSection checkSection = config.getConfigurationSection("checks");

        checkSection.getKeys(false).forEach(key -> {
            boolean enabled = checkSection.getBoolean(key + ".enabled");

            if (enabled) {
                enabledChecks.add(key);
            }

            boolean setback = checkSection.getBoolean(key + ".setback");

            if (setback) {
                setbackChecks.add(key);
            }

            thresholds.put(key, checkSection.getInt(key + ".maximum-vl"));
            punishCommands.put(key, checkSection.getString(key + ".punish-command"));
        });

        loaded = true;
    }

    private long timeBetweenAlerts;
    private boolean broadcastPunishment;
    private long transactionResponseTimeout;
    private boolean creativeBypass;

    private String broadcastPunishmentFormat;
    private String alertFormat;
    private String alertHoverFormat;
    private String toggledAlertsOn;
    private String toggledAlertsOff;
    private List<String> infoCommand;
    private List<String> invalidArguments;
    private String argumentResponseColor;
    private String commandResponseHeader;
    private String commandResponseColor;

    private final Set<String> enabledChecks = new HashSet<>();
    private final Set<String> setbackChecks = new HashSet<>();
    private final Map<String, String> punishCommands = new HashMap<>();
    private final Map<String, Integer> thresholds = new HashMap<>();
}
