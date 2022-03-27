package com.gladurbad.ares.task;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.task.impl.ReachTask;
import com.gladurbad.ares.task.impl.TransactionTask;
import com.gladurbad.ares.task.type.PostTickTask;
import com.gladurbad.ares.task.type.PreTickTask;
import com.gladurbad.ares.util.container.HeterogeneousContainer;
import com.gladurbad.ares.util.nms.NmsUtil;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskManager extends BukkitRunnable {

    @Getter
    private int tick;
    @Getter
    private final HeterogeneousContainer taskContainer = new HeterogeneousContainer();

    private long lastTickTime;

    @Getter
    private double tps;
    @Getter
    private int lagTick;

    private boolean ticking;

    public TaskManager() {

        List<Task> tasks = new ArrayList<>(Arrays.asList(
                new ReachTask(),
                new TransactionTask()
        ));

        for (Task task : tasks) {
            for (Class<?> ifc : task.getClass().getInterfaces()) {
                taskContainer.put(task, ifc);

                if (ifc == PostTickTask.class) {
                    NmsUtil.insertPostTickTask((PostTickTask) task);
                }
            }
        }

    }

    public void start() {
        this.runTaskTimer(Ares.INSTANCE.getPlugin(), 0L, 1L);
        ticking = true;
    }

    public void destroy() {
        if (this.ticking) {
            this.cancel();
        }
    }

    @Override
    public void run() {
        ++tick;
        ++lagTick;

        taskContainer.get(PreTickTask.class).forEach(task -> {
            task.handlePreTick();

            Ares.INSTANCE.getPlayerDataManager().getData().forEach(data -> data.getTrackerManager().handleTask(task));
        });

        long now = System.currentTimeMillis();
        long delta = now - lastTickTime;

        tps = Math.min(20.0, (delta / 50.0) * 20.0);

        if (tps < 19) lagTick = 0;

        lastTickTime = now;
    }
}
