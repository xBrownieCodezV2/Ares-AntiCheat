package com.gladurbad.ares.check.impl.aim;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AimCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.math.MathUtil;

@CheckInfo(name = "Aim A")
public class AimA extends AimCheck {

    public AimA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        // Grab the current yaw and pitch.
        float yaw = to.getYaw();
        float pitch = to.getPitch();

        // Grab the deltaYaw, we will use this to derive mouse delta X.
        float deltaYaw = motion.getYaw();

        // Grab the current and last delta pitch, we use these to derive mouse delta Y and get pitch divisor.
        float deltaPitch = motion.getPitch();
        float lastDeltaPitch = lastMotion.getPitch();

        // Create the pitch divisor, yaw checking is much more inaccurate.
        double divisor = MathUtil.getGcd(deltaPitch, lastDeltaPitch);

        // If the divisor is too low, this means the player is not using a GCD fix or they are using cinematic.
        if (divisor < 0.0078125F) return;

        // Create the rough mouse delta X and Y.
        double deltaX = deltaYaw / divisor;
        double deltaY = deltaPitch / divisor;

        // Check if the rounding is not too imprecise on the mouse deltas.
        boolean properX = Math.abs(Math.round(deltaX) - deltaX) < 0.0001D;
        boolean properY = Math.abs(Math.round(deltaY) - deltaY) < 0.0001D;

        // Return if the rounding is not precise enough or if the player has not attacked or placed.
        if (!properX || !properY || (attackTicks.passed(10) && placeTicks.passed(10))) return;

        // Create the value patch check, this should patch some auras which have a bad mouse rounding.
        double diffX = Math.abs(yaw - (yaw - (yaw % divisor)));
        double diffY = Math.abs(pitch - (pitch - (pitch % divisor)));

        // Flag if the difference X and Y is too small, teleporting may false this so exempt it.
        if (diffX < 1e-4 && diffY < 1e-4 && teleportTicks.passed(data.getConnectionTracker().getPingTicks())) {
            if (incrementBuffer() > 5) {
                fail(
                        new Debug<>("diffX", diffX),
                        new Debug<>("diffY", diffY),
                        new Debug<>("teleportTicks", teleportTicks.getTicks())
                );
            }
        } else {
            decreaseBuffer(0.25);
        }
    }
}
