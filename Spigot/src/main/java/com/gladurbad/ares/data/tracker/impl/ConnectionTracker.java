package com.gladurbad.ares.data.tracker.impl;

import ac.artemis.packet.wrapper.Packet;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import cc.ghast.packet.PacketAPI;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientTransaction;
import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerTransaction;
import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.tracker.Tracker;
import com.gladurbad.ares.data.tracker.type.PacketHandler;
import com.gladurbad.ares.task.impl.TransactionTask;
import com.gladurbad.ares.util.confirm.PacketConfirmedAction;
import com.gladurbad.ares.util.math.MathUtil;
import com.gladurbad.ares.util.tick.TickTimer;
import lombok.Getter;

import java.util.*;

public class ConnectionTracker extends Tracker implements PacketHandler {

    public ConnectionTracker(PlayerData data) {
        super(data);
    }

    private final TransactionTask transactionTask = Ares.INSTANCE.getTaskManager()
            .getTaskContainer()
            .getSpecific(TransactionTask.class);

    private final Map<Short, Long> transactionMap = new HashMap<>();
    private final Deque<Short> transactionDeque = new LinkedList<>();
    private final Map<Short, List<PacketConfirmedAction>> actionMap = new HashMap<>();

    private long lastFlying, lastDelta;

    @Getter
    private long ping;
    @Getter
    private final TickTimer lagTick = new TickTimer();

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof GPacketPlayClientTransaction) {
            GPacketPlayClientTransaction transaction = (GPacketPlayClientTransaction) packet;

            short id = transaction.getActionNumber();

            if (transactionMap.containsKey(id)) {
                ping = System.currentTimeMillis() - transactionMap.get(id);

                actionMap.get(id).forEach(PacketConfirmedAction::run);
                actionMap.remove(id);

                int expected = transactionDeque.getFirst();

                if (expected != id) {
                    // TODO: Seems to have problems with bungee.
                    //data.monkey("Invalid response T2 (e=" + expected + " i=" + id + ")");
                }

                transactionDeque.removeFirst();
            } else {
                // TODO: Seems to have problems with bungee.
                //data.monkey("Invalid response T1 (" + id + ")");
            }

            transactionMap.remove(transaction.getActionNumber());
        } else if (packet instanceof GPacketPlayServerTransaction) {
            GPacketPlayServerTransaction transaction = (GPacketPlayServerTransaction) packet;

            short id = transaction.getActionNumber();

            actionMap.putIfAbsent(id, new ArrayList<>());
            transactionMap.putIfAbsent(id, System.currentTimeMillis());
            transactionDeque.add(id);

            if ((transactionMap.size() / 40.0) > Ares.INSTANCE.getConfigManager().getTransactionResponseTimeout()) {
                data.monkey("Time out (" + transactionMap.size() + ")");
            }

        } else if (packet instanceof PacketPlayClientFlying) {
            lagTick.tick();

            long now = System.currentTimeMillis();
            long delta = now - lastFlying;

            if (lastDelta > 100L && delta < 5L) lagTick.reset();

            lastDelta = delta;
            lastFlying = now;
        }
    }

    // Use to confirm things that are sent on the server tick.
    public void confirm(PacketConfirmedAction action) {
        short postTick = transactionTask.getPostTick();

        if (!actionMap.containsKey(postTick)) {
            actionMap.put(postTick, new ArrayList<>());
        }

        actionMap.get(postTick).add(action);
    }

    // Use for things not sent on the server tick.
    public void tickAndConfirm(PacketConfirmedAction action) {
        short tick = transactionTask.getNextTick();

        PacketAPI.sendPacket(data.getPlayer(), new GPacketPlayServerTransaction((byte) 0, tick, false));

        if (!actionMap.containsKey(tick)) {
            actionMap.put(tick, new ArrayList<>());
        }

        actionMap.get(tick).add(action);
    }

    public int getPingTicks() {
        return Math.min(50, MathUtil.getPingTicks(ping, 10));
    }
}
