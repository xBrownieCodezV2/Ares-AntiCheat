/*package com.gladurbad.ares.check.impl.speed;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.attribute.AttributeType;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.math.MathUtil;
import com.gladurbad.ares.util.move.MovementUtil;
import lombok.val;
import org.bukkit.util.Vector;

@CheckInfo(name = "Speed B")
public class SpeedB extends MotionCheck {
    private static final double JUMP_FACTOR = 0.02;

    private double lastHorizontal = 0.0;
    private double lastRawHorizontal = 0.0;

    public SpeedB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        final double deltaX = to.getX();
        final double deltaZ = to.getZ();

        final double horizontal = Math.hypot(deltaX, deltaZ);
        float slipperiness = 0.91F;

        final boolean ground = to.onGround;
        final boolean lastGround = from.onGround;

        final boolean exempt = slimeTicks.occurred(3)
                || data.getPlayer().getAllowFlight()
                || placeAffectMotionTicks.occurred(3)
                || teleportTicks.occurred(1)
                || data.getPlayer().isInsideVehicle() && data.getPlayer().getVehicle() != null
                || data.isBanning();

        if (lastGround) slipperiness = from.getSlipperiness(data.getPlayer().getWorld());
        double base = MovementUtil.getAttributeSpeed(data, true);

        if (lastGround) {
            base *= 1.3F;
            base *= 0.16277136F / (slipperiness * slipperiness * slipperiness);

            if (!ground) base += 0.2F;
        }

        else {
            base = 0.026F;
        }

        final double velocityAddition = velocityTicks.occurred(3) ? Math.hypot(velocity.getX(), velocity.getZ()) : 0.0;
        base += velocityAddition;

        double speed = (horizontal - lastHorizontal) / base;
        if (nonUpdateTicks.occurred(3)) base += 0.25;

        if (speed > 1.005 && !exempt) {
            if (increaseBuffer(speed / 1.005) > 2) {
                this.fail(
                        new Debug<>("base", base),
                        new Debug<>("horizontal", horizontal),
                        new Debug<>("velocity", velocityAddition),
                        new Debug<>("slipperiness", slipperiness));
            }
        }

        fly_handle: {
            if (lastGround || !ground || !exempt) break fly_handle;

            double estimate = lastRawHorizontal * 0.91F;
            val factor = JUMP_FACTOR + (JUMP_FACTOR * 0.3D);

            if (Math.abs(horizontal - estimate) > factor && estimate > 0.1 && estimate > 0.075) {
                if (increaseBuffer(Math.abs(horizontal - estimate) / factor) > 1.0) {
                    this.fail(
                            new Debug<>("factor", factor),
                            new Debug<>("offset", Math.abs(horizontal - estimate)),
                            new Debug<>("estimate", estimate)
                    );
                }
            }
        }

        this.decreaseBuffer(0.01);

        this.lastHorizontal = horizontal * slipperiness;
        this.lastRawHorizontal = horizontal;
    }
}
*/