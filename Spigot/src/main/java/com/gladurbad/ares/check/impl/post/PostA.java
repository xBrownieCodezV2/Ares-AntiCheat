package com.gladurbad.ares.check.impl.post;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.PostCheck;
import com.gladurbad.ares.data.PlayerData;
import net.minecraft.server.v1_8_R1.PacketPlayInArmAnimation;

@CheckInfo(name = "Post A")
public class PostA extends PostCheck {

    public PostA(PlayerData data) {
        super(data, packet -> packet instanceof PacketPlayInArmAnimation);
    }
}
