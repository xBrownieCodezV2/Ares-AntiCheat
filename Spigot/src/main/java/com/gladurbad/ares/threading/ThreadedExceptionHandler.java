package com.gladurbad.ares.threading;

import org.bukkit.Bukkit;

public class ThreadedExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Bukkit.getLogger().severe("[Ares Exception Handler] Internal exception on thread " + t.getName()
                + " ("
                + "priority: " + t.getPriority() + " "
                + "status: " + t.getState() + " "
                + "id: " + t.getId() + " "
                + ") "
        );

        e.printStackTrace();
    }
}
