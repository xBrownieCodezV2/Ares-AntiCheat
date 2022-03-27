package com.gladurbad.ares.check.impl.scaffold;

import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientHeldItemSlot;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PostCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "Scaffold (B)")
public class ScaffoldB extends PostCheck {

    public ScaffoldB(PlayerData data) {
        super(data, packet -> packet instanceof GPacketPlayClientHeldItemSlot);
    }
}
