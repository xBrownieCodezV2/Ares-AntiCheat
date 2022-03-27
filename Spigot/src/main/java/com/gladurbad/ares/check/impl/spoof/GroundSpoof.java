package com.gladurbad.ares.check.impl.spoof;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;

@CheckInfo(name = "GroundSpoof")
public class GroundSpoof extends MotionCheck {

    public GroundSpoof(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        final double deltaY = to.getY() - from.getY();

        final boolean ground = to.isOnGround();
        final boolean math = to.getY() % 0.015625 == 0.0;

        final boolean step = to.getY() % 0.015625 == 0.0 && deltaY > 0.0;

        // A
        if (!step && deltaY > 1.0) this.fail();

        // B
        if (math != ground) {
            if (this.increaseBuffer(1.0) > 4) this.fail();
        }

        this.decreaseBuffer(0.25F);
    }
}
