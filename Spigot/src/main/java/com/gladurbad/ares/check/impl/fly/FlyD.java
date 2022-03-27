package com.gladurbad.ares.check.impl.fly;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.attribute.AttributeType;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "Fly D")
public class FlyD extends MotionCheck {
    private double total = 0.0;

    public FlyD(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        if (airTicks.occurred(5) || to.isOnGround() || from.isOnGround()) return;

        check_handle:
        {
            if (from.getY() > to.getY() || velocityTicks.occurred(3)) {
                decreaseBuffer(0.1);
                total = 0.0;

                break check_handle;
            }

            int modifier = attributes.get(AttributeType.JUMP).getLevel();

            double delta = motion.getY();
            double threshold = modifier > 0 ? 1.55220341408 + (Math.pow(modifier + 4.2, 2D) / 16D) : 1.25220341408;

            total += delta;

            if (total > threshold) {
                if (incrementBuffer() > 6) this.fail(
                        new Debug<>("total", total),
                        new Debug<>("threshold", threshold)
                );
            } else buffer = Math.max(1.0, buffer / 2);
        }
    }
}
