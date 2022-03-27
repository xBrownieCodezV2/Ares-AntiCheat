package com.gladurbad.ares.check.impl.badpackets;

import ac.artemis.packet.wrapper.Packet;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PacketCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "BadPackets (C)")
public class BadPacketsC extends PacketCheck {
    private int streak;

    public BadPacketsC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof PacketPlayClientFlying) {
            final PacketPlayClientFlying flying = (PacketPlayClientFlying) packet;

            if (data.getMotionTracker().getTeleportTicks().occurred(3)) return;

            if (flying.isPos()) streak = 0;

            else {
                if (++streak > 20) this.fail();
            }
        }
    }
}
