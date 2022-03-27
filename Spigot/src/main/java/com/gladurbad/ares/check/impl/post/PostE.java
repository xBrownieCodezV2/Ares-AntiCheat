package com.gladurbad.ares.check.impl.post;

import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientEntityAction;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PostCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "Post (E)")
public class PostE extends PostCheck {

    public PostE(PlayerData data) {
        super(data, packet -> packet instanceof GPacketPlayClientEntityAction);
    }
}
