package com.gladurbad.ares.check.impl.badpackets;

import ac.artemis.packet.wrapper.Packet;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import cc.ghast.packet.wrapper.mc.PlayerEnums;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientArmAnimation;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientUseEntity;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PacketCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "BadPackets (F)")
public class BadPacketsF extends PacketCheck {
    private boolean action = false;

    public BadPacketsF(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof GPacketPlayClientUseEntity) {
            final GPacketPlayClientUseEntity use = (GPacketPlayClientUseEntity) packet;

            if (use.getType() == PlayerEnums.UseType.ATTACK && !action) this.fail();
        } else if (packet instanceof GPacketPlayClientArmAnimation) {
            action = true;
        } else if (packet instanceof PacketPlayClientFlying) {
            action = false;
        }
    }
}
