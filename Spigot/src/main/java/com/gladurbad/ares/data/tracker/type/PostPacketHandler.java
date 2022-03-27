package com.gladurbad.ares.data.tracker.type;

import ac.artemis.packet.wrapper.Packet;

public interface PostPacketHandler {
    void handlePostPacket(Packet packet);
}
