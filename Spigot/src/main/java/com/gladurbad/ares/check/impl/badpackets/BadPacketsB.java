package com.gladurbad.ares.check.impl.badpackets;

import ac.artemis.packet.wrapper.Packet;
import cc.ghast.packet.wrapper.mc.PlayerEnums;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientBlockDig;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientFlying;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientUseEntity;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PacketCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "BadPackets (B)")
public class BadPacketsB extends PacketCheck {
    private PlayerEnums.DigType lastType = null;
    private int ticks;
    private boolean act = false;

    public BadPacketsB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet instanceof GPacketPlayClientBlockDig) {
            final GPacketPlayClientBlockDig dig = (GPacketPlayClientBlockDig) packet;

            if (++ticks > 1 && dig.getType() == lastType) fail();

            if (dig.getType() == PlayerEnums.DigType.RELEASE_USE_ITEM) act = true;

            this.lastType = dig.getType();
        } else if (packet instanceof GPacketPlayClientUseEntity) {
            final GPacketPlayClientUseEntity use = (GPacketPlayClientUseEntity) packet;

            if (use.getType() == PlayerEnums.UseType.ATTACK && act) fail();
        } else if (packet instanceof GPacketPlayClientFlying) {
            ticks = 0;
            act = false;
        }
    }
}
