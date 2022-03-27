package com.gladurbad.ares.check.impl.scaffold;

import ac.artemis.packet.wrapper.Packet;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientHeldItemSlot;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PacketCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "Scaffold (A)")
public class ScaffoldA extends PacketCheck {
    private int lastSlot;

    public ScaffoldA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof GPacketPlayClientHeldItemSlot) {
            GPacketPlayClientHeldItemSlot inst = (GPacketPlayClientHeldItemSlot) packet;

            final int slot = inst.getSlot();

            if (slot == lastSlot) this.fail();

            this.lastSlot = inst.getSlot();
        }
    }
}
