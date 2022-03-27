package com.gladurbad.ares.check.impl.fly;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.math.MathUtil;

@CheckInfo(name = "Fly C")
public class FlyC extends MotionCheck {

    public FlyC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        // Exempt cases where a basic check like this could false.
        if (halfBlockTicks.occurred(10)
                || liquidTicks.occurred(10)
                || slimeTicks.occurred(10)
                || teleportTicks.occurred(1)
                || ticksExisted.occurred(100))
            return;

        // When the player claims they are on the ground 99% of the time their math ground will also be true.
        boolean mathGround = MathUtil.onGround(to.getY());
        boolean clientGround = to.isOnGround();

        double motionY = Math.abs(motion.getY());

        boolean onGroundSpoof = clientGround && !mathGround && motionY > 0;
        boolean noGroundSpoof = !clientGround && mathGround && motionY == 0;

        // Flag if the two values don't match.
        if (onGroundSpoof || noGroundSpoof) {
            if (incrementBuffer() > 4) {
                fail(
                        new Debug<>("mathGround", mathGround),
                        new Debug<>("clientGround", clientGround),
                        new Debug<>("motionY", motion.getY()),
                        new Debug<>("distance", to.distance(from))
                );
            }
        } else {
            decreaseBuffer(0.05);
        }
    }
}
