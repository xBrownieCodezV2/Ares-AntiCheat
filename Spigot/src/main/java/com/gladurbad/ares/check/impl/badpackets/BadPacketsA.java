package com.gladurbad.ares.check.impl.badpackets;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "BadPackets A")
public class BadPacketsA extends MotionCheck {

    public BadPacketsA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        // Your pitch cannot go above 90.0 or below -90.0.
        if (Math.abs(to.getPitch()) > 90.0F)
            fail(new Debug<>("pitch", to.getPitch()));
    }
}
