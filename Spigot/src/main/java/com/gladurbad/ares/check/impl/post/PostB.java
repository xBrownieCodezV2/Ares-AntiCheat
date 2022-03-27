package com.gladurbad.ares.check.impl.post;

import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientBlockPlace;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PostCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "Post B")
public class PostB extends PostCheck {

    public PostB(PlayerData data) {
        super(data, packet -> packet instanceof GPacketPlayClientBlockPlace);
    }

}
