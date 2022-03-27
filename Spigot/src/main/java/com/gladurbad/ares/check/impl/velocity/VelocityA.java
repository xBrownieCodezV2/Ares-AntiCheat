package com.gladurbad.ares.check.impl.velocity;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.attribute.AttributeType;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "Velocity A")
public class VelocityA extends MotionCheck {

    public VelocityA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        // TODO: Sometimes the velocity tick can be off (99% of the time it's not) but it can cause a false.
        if (velocityTicks.occurred(1)) {
            double ratio = motion.getY() / velocity.getY();

            boolean exempt = underBlockTicks.occurred(5)
                    || webTicks.occurred(5)
                    || ladderTicks.occurred(5)
                    || liquidTicks.occurred(5)
                    || teleportTicks.occurred(1)
                    || data.getPlayer().isInsideVehicle()
                    || velocity.getY() < 0;

            if (exempt) return;

            double expectedJump = 0.42F + (attributes.get(AttributeType.JUMP).getLevel() * 0.1F);

            if (expectedJump == motion.getY()) return;

            if (Math.abs(1.0 - ratio) > 0.005) {
                if (incrementBuffer() > 3) {
                    fail(new Debug<>("ratio", ratio),
                            new Debug<>("velocity", velocity.getY()),
                            new Debug<>("motion", motion.getY()));
                }
            } else {
                decreaseBuffer(0.05);
            }
        }
    }
}
