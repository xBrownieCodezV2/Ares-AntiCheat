package com.gladurbad.ares.check.impl.post;

import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientSteerVehicle;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PostCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "Post (F)")
public class PostF extends PostCheck {

    public PostF(PlayerData data) {
        super(data, packet -> packet instanceof GPacketPlayClientSteerVehicle);
    }
}
