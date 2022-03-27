package com.gladurbad.ares.check.impl.post;

import cc.ghast.packet.wrapper.mc.PlayerEnums;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientUseEntity;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PostCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "Post A")
public class PostC extends PostCheck {

    public PostC(PlayerData data) {
        super(data, packet -> packet instanceof GPacketPlayClientUseEntity
                && ((GPacketPlayClientUseEntity) packet).getType() == PlayerEnums.UseType.ATTACK);
    }
}
