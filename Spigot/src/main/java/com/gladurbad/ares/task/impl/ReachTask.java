package com.gladurbad.ares.task.impl;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.task.type.PostTickTask;
import org.bukkit.entity.Entity;

public class ReachTask implements PostTickTask {

    @Override
    public void handlePostTick() {
        int tick = Ares.INSTANCE.getTaskManager().getTick();

        Ares.INSTANCE
                .getPlayerDataManager()
                .getData()
                .forEach(data -> {
                    Entity entity = data.getCombatTracker().getEntity();

                    if (entity != null) {
                        data.getCombatTracker().tick(tick, entity);
                    }
                });
    }

}
