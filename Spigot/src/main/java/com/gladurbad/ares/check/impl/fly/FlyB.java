package com.gladurbad.ares.check.impl.fly;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "Fly B")
public class FlyB extends MotionCheck {

    public FlyB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        // Check if the player claims they are on ground even though they have been in the air for too long.
        if (airTicks.passed(10)
                && to.isOnGround()
                && teleportTicks.passed(1)
                && placeAffectMotionTicks.passed(5)
                && ticksExisted.passed(100))
            fail(new Debug<>("serverAirTicks", airTicks.getTicks()));
    }
}
