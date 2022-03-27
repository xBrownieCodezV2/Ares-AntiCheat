package com.gladurbad.ares.data.tracker.type;

import ac.artemis.packet.wrapper.Packet;

public interface PacketHandler {
    void handlePacket(Packet packet);
}
