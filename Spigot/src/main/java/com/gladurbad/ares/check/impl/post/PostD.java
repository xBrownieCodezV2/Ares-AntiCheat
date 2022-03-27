package com.gladurbad.ares.check.impl.post;

import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientAbilities;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PostCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "Post (D)")
public class PostD extends PostCheck {

    public PostD(PlayerData data) {
        super(data, packet -> packet instanceof GPacketPlayClientAbilities);
    }
}
