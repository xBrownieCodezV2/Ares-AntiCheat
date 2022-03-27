package com.gladurbad.ares.check.impl.badpackets;

import ac.artemis.packet.wrapper.Packet;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientHeldItemSlot;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PacketCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "BadPackets (H)")
public class BadPacketsH extends PacketCheck {
    public BadPacketsH(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof GPacketPlayClientHeldItemSlot) {
            final GPacketPlayClientHeldItemSlot slot = (GPacketPlayClientHeldItemSlot) packet;

            if (slot.getSlot() < 0 || slot.getSlot() > 9) this.fail();
        }
    }
}
