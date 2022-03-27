package com.gladurbad.ares.check.type;

import ac.artemis.packet.wrapper.Packet;
import com.gladurbad.ares.check.AbstractCheck;
import com.gladurbad.ares.data.PlayerData;

public abstract class PacketCheck extends AbstractCheck {

    public PacketCheck(PlayerData data) {
        super(data);
    }

    public abstract void handle(Packet packet);
}
