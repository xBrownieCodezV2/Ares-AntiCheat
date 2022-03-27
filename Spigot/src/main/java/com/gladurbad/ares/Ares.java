package com.gladurbad.ares;

import cc.ghast.packet.PacketManager;
import com.gladurbad.ares.bungee.BungeeMessenger;
import com.gladurbad.ares.check.CheckManager;
import com.gladurbad.ares.config.ConfigManager;
import com.gladurbad.ares.data.PlayerDataManager;
import com.gladurbad.ares.gui.GUIManager;
import com.gladurbad.ares.listener.CommandListener;
import com.gladurbad.ares.listener.EventListener;
import com.gladurbad.ares.packet.PacketParser;
import com.gladurbad.ares.task.TaskManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public enum Ares {

    @Getter(AccessLevel.NONE)
    INSTANCE;

    private boolean enabled;

    private AresPlugin plugin;

    private PlayerDataManager playerDataManager;
    private PacketParser packetParser;
    private EventListener eventListener;
    private TaskManager taskManager;
    private CommandListener commandListener;
    private ConfigManager configManager;
    private GUIManager guiManager;
    private BungeeMessenger bungeeMessenger;
    private CheckManager checkManager;

    public void start(AresPlugin plugin) {
        // Set the plugin.
        this.plugin = plugin;

        // Save the spigot config.
        this.plugin.saveDefaultConfig();

        // Start the packet manager.
        PacketManager.INSTANCE.init(plugin);

        // Initialize all core aspect.
        eventListener = new EventListener();
        playerDataManager = new PlayerDataManager();
        taskManager = new TaskManager();
        packetParser = new PacketParser();
        commandListener = new CommandListener();
        configManager = new ConfigManager();
        checkManager = new CheckManager();
        guiManager = new GUIManager();
        bungeeMessenger = new BungeeMessenger(this.plugin);

        // Load in the config values.
        configManager.load();

        // Start the SmartInvs gui manager.
        guiManager.init();

        // Set ares command executor.
        this.plugin.getCommand("ares").setExecutor(commandListener);

        // Open bungeecord messaging channels for ares we use these for bungee alerts and punishments.
        Bukkit.getMessenger().registerOutgoingPluginChannel(this.plugin, "AresBungee");
        Bukkit.getMessenger().registerIncomingPluginChannel(this.plugin, "AresBungee", bungeeMessenger);

        // Start the task manager.
        taskManager.start();

        enabled = true;
    }

    public void stop(AresPlugin plugin) {
        enabled = false;

        // Set the plugin.
        this.plugin = plugin;

        PacketManager.INSTANCE.destroy();

        // Stop the task manager.
        taskManager.destroy();

        // Remove bungeecord channels.
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this.plugin);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this.plugin);
    }

    public boolean isLagging() {
        return taskManager.getLagTick() < 15;
    }
}
