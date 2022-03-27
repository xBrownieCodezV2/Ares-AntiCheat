package com.gladurbad.ares.check;

import com.gladurbad.ares.alert.AlertManager;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import lombok.Getter;
import org.atteo.classindex.IndexSubclasses;
import org.bukkit.Bukkit;

@Getter
@IndexSubclasses
public class AbstractCheck {

    private final AlertManager alertManager;
    private String name;
    protected final PlayerData data;

    protected double buffer;

    public AbstractCheck(PlayerData data) {
        this.data = data;

        alertManager = new AlertManager(data, this);

        Class<?> clazz = this.getClass();

        if (clazz.isAnnotationPresent(CheckInfo.class)) {
            CheckInfo info = clazz.getAnnotation(CheckInfo.class);

            name = info.name();
        } else {
            System.out.println("No CheckInfo in " + clazz.getSimpleName());
        }
    }

    protected void fail(double vl, Debug<?>... debug) {
        alertManager.fail(vl, debug);
    }

    protected void fail(Debug<?>... debug) {
        alertManager.fail(1.0, debug);
    }

    protected void fail(double vl) {
        alertManager.fail(vl, new Debug<>("", ""));
    }

    protected void fail() {
        alertManager.fail(1.0, new Debug<>("", ""));
    }

    protected void debug(Object debug) {
        Bukkit.broadcastMessage(String.valueOf(debug));
    }

    protected double increaseBuffer(double amount) {
        return buffer = Math.min(buffer + amount, 20);
    }

    protected double incrementBuffer() {
        return increaseBuffer(1.0);
    }

    protected double decreaseBuffer(double amount) {
        return buffer = Math.max(buffer - amount, 0);
    }

    protected double decrementBuffer() {
        return decreaseBuffer(1.0);
    }

    protected void cancelCombat(int ticks) {
        data.getCombatTracker().setCancelTicks(ticks);
    }

    protected void resetBuffer() {
        buffer = 0.0;
    }
}
