package com.gladurbad.ares.check.impl.step;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "Step A")
public class StepA extends MotionCheck {

    public StepA(PlayerData data) {
        super(data);
    }

    private double lastGround, total;
    private boolean set;

    @Override
    public void handle() {
        if (to.isOnGround()) {
            total += Math.abs(motion.getY());
            double distance = to.getY() - lastGround;

            if (set && distance >= 1.0 && total <= 1.15
                    && teleportTicks.passed(data.getConnectionTracker().getPingTicks())
                    && halfBlockTicks.passed(5)
                    && velocityTicks.passed(5)
                    && ticksExisted.passed(100)) {
                fail(
                        new Debug<>("distance", distance),
                        new Debug<>("total", total),
                        new Debug<>("teleportTicks", teleportTicks.getTicks())
                );
            }

            lastGround = to.getY();
            set = true;
            total = 0;
        } else {
            total += Math.abs(motion.getY());
        }
    }
}
