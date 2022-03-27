package com.gladurbad.ares.check.impl.timer;

import ac.artemis.packet.wrapper.Packet;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerPosition;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PacketCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "Timer A")
public class TimerA extends PacketCheck {

    public TimerA(PlayerData data) {
        super(data);
    }

    private long balance = -50, lastFlying, ticks, lastPacket;
    private boolean read;

    @Override
    public void handle(Packet packet) {
        if (packet instanceof PacketPlayClientFlying) {
            long now = System.currentTimeMillis();

            if (++ticks > 200 && lastFlying != 0L) {
                long delta = now - lastFlying;

                balance += 50L;
                balance -= delta;

                if (balance > 50L) {
                    if (incrementBuffer() > 4) {
                        fail(
                                new Debug<>("delta", delta),
                                new Debug<>("balance", balance)
                        );
                    }

                    balance = -50L;
                } else {
                    decreaseBuffer(0.00001);
                }
            }

            lastFlying = now;
        } else if (packet instanceof GPacketPlayServerPosition) {
            balance -= 50L;
        }
    }
}
