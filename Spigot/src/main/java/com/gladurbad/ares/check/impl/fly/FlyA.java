package com.gladurbad.ares.check.impl.fly;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "Fly A")
public class FlyA extends MotionCheck {

    public FlyA(PlayerData data) {
        super(data);
    }

    private int invalidTick;

    @Override
    public void handle() {
        // Check if the player is not on client ground for 2 ticks or 5 ticks server-side in case of ground-spoof.n
        if (airTicks.passed(5) || (!to.isOnGround() && !from.isOnGround() && underBlockTicks.passed(5))) {
            // Create a rudimentary vertical prediction since the player is floating.
            double estimation = (lastMotion.getY() - 0.08D) * 0.98F;

            // Floor the estimation because the game does this after the tick.
            if (Math.abs(estimation) < 0.005) estimation = 0.0D;

            // Create an offset.
            double offset = Math.abs(motion.getY() - estimation);

            if (motion.getHorizontalDistance() < 0.02 && Math.abs(motion.getY()) < 0.02) invalidTick = 0;

            // Check if the offset is too high and that they are not in liquids since ground state is false in liquids.
            if (offset > 1e-10 && liquidTicks.passed(5) && teleportTicks.passed(1)
                    && velocityTicks.passed(1) && ++invalidTick > 5 && !isFlying()) {
                if (incrementBuffer() > 2) {
                    fail(
                            new Debug<>("offset", offset),
                            new Debug<>("estimation", estimation),
                            new Debug<>("real", motion.getY()),
                            new Debug<>("predict", estimation),
                            new Debug<>("liquidTicks", liquidTicks.getTicks()),
                            new Debug<>("teleportTicks", teleportTicks.getTicks()),
                            new Debug<>("serverAirTicks", airTicks.getTicks()),
                            new Debug<>("velocityTicks", velocityTicks.getTicks()),
                            new Debug<>("nonUpdateTicks", nonUpdateTicks.getTicks())
                    );
                }
            } else {
                decreaseBuffer(0.005);
            }
        }
    }
}
