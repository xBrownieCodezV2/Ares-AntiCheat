package com.gladurbad.ares.check.impl.aim;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AimCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "Aim E")
public class AimE extends AimCheck {

    public AimE(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        // You cannot send a rotation packet without actually rotating unless you phase or teleport.
        if (teleportTicks.passed(100)
                && motion.getYaw() + motion.getPitch() == 0.0
                && !data.getPlayer().isInsideVehicle()) {
            if (incrementBuffer() > 3) {
                fail(new Debug<>("teleportTicks", teleportTicks.getTicks()));
            }
        } else {
            resetBuffer();
        }
    }
}
