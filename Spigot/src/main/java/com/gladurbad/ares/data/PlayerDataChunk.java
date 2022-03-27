package com.gladurbad.ares.data;

import com.gladurbad.ares.threading.Threading;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Getter
public class PlayerDataChunk {
    private final String name;
    private final int size;
    private final List<PlayerData> playerData;
    private final ExecutorService service;

    public PlayerDataChunk(String name, int size) {
        this.name = name;
        this.size = size;
        this.playerData = new ArrayList<>();
        this.service = Threading.createIfAbsent(name);
    }

    public boolean canAdd() {
        return playerData.size() < size;
    }

    public boolean isEmpty() {
        return playerData.isEmpty();
    }

    public void add(PlayerData data) {
        this.playerData.add(data);
        data.setService(this);
    }

    public void remove(PlayerData data) {
        this.playerData.remove(data);
        data.setService(null);
    }

    public ExecutorService getExecutor() {
        return service;
    }
}
