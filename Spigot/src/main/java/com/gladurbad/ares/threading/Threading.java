package com.gladurbad.ares.threading;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class Threading {
    private static final Map<String, ExecutorService> threads = new WeakHashMap<>();
    private static final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("ares-threading-%d")
            .setPriority(7)
            .setUncaughtExceptionHandler(new ThreadedExceptionHandler()).build();

    public static ExecutorService createIfAbsent(String name) {
        if (threads.containsKey(name))
            return threads.get(name);

        ExecutorService thread = Executors.newSingleThreadExecutor(threadFactory);

        threads.put(name, thread);
        return thread;
    }

    public static void killThread(String name) {
        ExecutorService thread = threads.get(name);

        if (thread != null) {
            thread.shutdown();
            threads.remove(name);
        }
    }
}
