package com.gladurbad.ares.task.impl;

import cc.ghast.packet.PacketAPI;
import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerTransaction;
import com.gladurbad.ares.Ares;
import com.gladurbad.ares.task.type.PostTickTask;
import com.gladurbad.ares.task.type.PreTickTask;
import lombok.Getter;

@Getter
public class TransactionTask implements PreTickTask, PostTickTask {

    private short preTick, postTick;
    private int tick;

    @Override
    public void handlePreTick() {
        tick++;

        if (tick > 0) {
            preTick = (short) (-(tick * 2) - 1 % Short.MAX_VALUE);
            postTick = (short) (-(tick * 2) % Short.MAX_VALUE);

            Ares.INSTANCE
                    .getPlayerDataManager()
                    .getData()
                    .forEach(data -> PacketAPI.sendPacket(data.getPlayer(), new GPacketPlayServerTransaction((byte) 0, preTick, false)));
        }
    }

    @Override
    public void handlePostTick() {
        Ares.INSTANCE
                .getPlayerDataManager()
                .getData()
                .forEach(data -> PacketAPI.sendPacket(data.getPlayer(), new GPacketPlayServerTransaction((byte) 0, postTick, false)));
    }

    public short getNextTick() {
        ++tick;
        return (short) (-(tick * 2) % Short.MAX_VALUE);
    }
}
