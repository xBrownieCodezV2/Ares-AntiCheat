package com.gladurbad.ares.alert;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.check.AbstractCheck;
import com.gladurbad.ares.config.ConfigManager;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.log.Log;
import com.gladurbad.ares.util.placeholder.AlertPlaceHolder;
import com.gladurbad.ares.util.string.StringUtil;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AlertManager {

    private final PlayerData data;
    private final AbstractCheck check;

    private final long threshold;
    private final String banCommand;

    private long lastFlag;

    private double vl;

    private final List<Log> logs = new ArrayList<>();

    private final String alertFormat = Ares.INSTANCE.getConfigManager().getAlertFormat();
    private final String broadcastFormat = Ares.INSTANCE.getConfigManager().getBroadcastPunishmentFormat();

    @Setter
    private boolean enabled = true;

    public AlertManager(PlayerData data, AbstractCheck check) {
        this.data = data;
        this.check = check;

        String className = check.getClass().getSimpleName();
        ConfigManager configManager = Ares.INSTANCE.getConfigManager();

        this.threshold = configManager.getThresholds().get(className);
        this.banCommand = configManager.getPunishCommands().get(className);
    }

    public void fail(double increment, Debug<?>... debug) {
        if (Ares.INSTANCE.getConfigManager().isCreativeBypass()
                && data.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;

        Runnable logExecution = () -> {
            /*
             * First, we will update the VL. The VL decay is dependent on system time, so it is required that
             * we update the VL before each time we poll it.
             *
             * There are a few steps to this.
             *
             * 1. Get the current system time.
             * 2. Remove the logs which have been created past the reset threshold time in the check.
             * 3. Add a new log.
             * 4. Get the total VL by adding the VL of all the logs together.
             */
            long now = System.currentTimeMillis();

            logs.removeIf(log -> now - log.getCreationTime() > 300000L);
            logs.add(new Log(increment));

            vl = logs.stream().mapToDouble(Log::getVl).sum();

            // Get the Bukkit player of the PlayerData since we need to check a few things in here.
            Player player = data.getPlayer();

            // Check if the server is not lagging and the VL can alert.
            // Check if the time since the lat flag has been long enough so we don't spam.
            if (now - lastFlag > 0) {
                // Copy the alert format and replace its placeholders.
                String log = AlertPlaceHolder.replacePlaceholders(check, alertFormat);

                // Create a TextComponent with the new log.
                TextComponent component = new TextComponent(log);

                // Create the command to be run when the component is clicked. TODO: Make this configurable.
                String command = "/tp " + player.getName();

                // Set the click event to run a teleport command to this player.
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));

                // Create the string of lines for the hover component. If they are a developer, add the debugs.
                String hover = AlertPlaceHolder.replacePlaceholders(check, Ares.INSTANCE.getConfigManager().getAlertHoverFormat())
                        .replaceAll("%DEBUG%", StringUtil.chainDebugs(debug));

                // Color the string
                hover = StringUtil.color(hover);

                // Set the hover event to show this text.
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));

                // Send the alert to all staff with alerts on.
                Ares.INSTANCE.getPlayerDataManager()
                        .getAlerts()
                        .stream()
                        .map(data -> data.getPlayer().spigot())
                        .forEach(spigot -> spigot.sendMessage(component));

                // Send the alert to the bungee cord proxy.
                ByteArrayDataOutput output = ByteStreams.newDataOutput();

                // Select the "sub channel" which in this case will be alert, so we will tell the recipients to alert.
                output.writeUTF("alert");

                // Send the actual log itself.
                output.writeUTF(log);

                // Send the hover component.
                output.writeUTF(hover);

                // Send the clickable command.
                output.writeUTF(command);

                // Send to all plugins connected to the proxy (besides this one? depends on how bungee works)
                Bukkit.getServer().sendPluginMessage(Ares.INSTANCE.getPlugin(), "AresBungee", output.toByteArray());

                lastFlag = now;
            }

            // Punish the player if they have exceeded the threshold.
            if (vl > threshold) {
                String formattedBroadcast = AlertPlaceHolder.replacePlaceholders(check, broadcastFormat);

                // Send the punishment command to the proxy.
                ByteArrayDataOutput output = ByteStreams.newDataOutput();

                String command = banCommand.replaceAll("%PLAYER_NAME%", player.getName());

                output.writeUTF("punish");
                output.writeUTF(command);

                Bukkit.getServer().sendPluginMessage(Ares.INSTANCE.getPlugin(), "AresBungee", output.toByteArray());

                Bukkit.getScheduler().runTask(Ares.INSTANCE.getPlugin(), () -> {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                });

                if (Ares.INSTANCE.getConfigManager().isBroadcastPunishment())
                    Bukkit.broadcastMessage(StringUtil.color(formattedBroadcast));

                logs.clear();
            }
        };

        // Send the runnable to the executor thread.
        if (enabled) {
            data.getService().getExecutor().execute(logExecution);
        }
    }
}
