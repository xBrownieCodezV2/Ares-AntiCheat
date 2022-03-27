package com.gladurbad.ares.check.impl.badpackets;

import ac.artemis.packet.wrapper.Packet;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientSteerVehicle;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PacketCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "BadPackets (D)")
public class BadPacketsD extends PacketCheck {

    public BadPacketsD(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(Packet packet) {
        if (packet instanceof GPacketPlayClientSteerVehicle) {
            final GPacketPlayClientSteerVehicle steer = (GPacketPlayClientSteerVehicle) packet;

            if (steer.getMoveForward() > 2.f || steer.getMoveStrafing() > 2.f || steer.getMoveForward() < 0.5 || steer.getMoveStrafing() < 0.5)
                this.fail();
        }
    }
}
