package com.gladurbad.ares.check.impl.badpackets;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "BadPackets (G)")
public class BadPacketsG extends MotionCheck {

    public BadPacketsG(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        final double x = to.getX();
        final double y = to.getY();
        final double z = to.getZ();

        if (Double.MAX_VALUE == x || Double.MAX_VALUE == y || Double.MAX_VALUE == z) {
            this.fail();
            data.getPlayer().kickPlayer("No");
        }
    }
}
