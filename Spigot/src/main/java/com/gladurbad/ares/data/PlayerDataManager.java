package com.gladurbad.ares.data;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.threading.Threading;
import com.google.common.collect.Maps;
import com.google.common.io.ByteArrayDataInput;
import lombok.Getter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class PlayerDataManager {

    private final Map<UUID, PlayerData> dataMap = Maps.newConcurrentMap();
    private final List<PlayerDataChunk> dataChunkList = new ArrayList<>();
    private final Set<PlayerData> alerts = new HashSet<>();

    public PlayerDataManager() {
        Bukkit.getOnlinePlayers().forEach(this::add);
    }

    public void add(Player player) {
        PlayerData data = new PlayerData(player);

        PlayerDataChunk chunk = dataChunkList.stream()
                .filter(PlayerDataChunk::canAdd)
                .min(Comparator.comparingDouble(PlayerDataChunk::getSize))
                .orElse(null);

        if (chunk == null) {
            PlayerDataChunk newChunk = new PlayerDataChunk("ares-data-chunk-" + dataChunkList.size() + 1, 50);
            dataChunkList.add(newChunk);
            newChunk.add(data);
        } else {
            chunk.add(data);
        }

        if (player.hasPermission("ares.alerts") || player.isOp()) {
            alerts.add(data);
        }

        dataMap.put(player.getUniqueId(), data);
    }

    public PlayerData get(Player player) {
        return dataMap.get(player.getUniqueId());
    }

    public PlayerData get(UUID player) {
        return dataMap.get(player);
    }

    public void remove(Player player) {
        PlayerData data = this.get(player);

        if (data != null) {
            PlayerDataChunk dataChunk = data.getService();
            dataChunk.remove(data);

            if (dataChunk.isEmpty()) {
                dataChunkList.remove(dataChunk);
                Threading.killThread(dataChunk.getName());
            }

            dataMap.remove(player.getUniqueId());
            alerts.remove(data);
        }
    }

    public void handleProxyAlert(ByteArrayDataInput input) {
        String log = input.readUTF();
        String hover = input.readUTF();
        String command = input.readUTF();

        TextComponent component = new TextComponent(log);

        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));

        alerts.stream()
                .map(data -> data.getPlayer().spigot())
                .forEach(spigot -> spigot.sendMessage(component));
    }

    public void handleProxyPunishment(ByteArrayDataInput input) {
        String command = input.readUTF();

        Bukkit.getScheduler().runTask(Ares.INSTANCE.getPlugin(), () -> {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        });
    }

    public Collection<PlayerData> getData() {
        return dataMap.values();
    }
}
