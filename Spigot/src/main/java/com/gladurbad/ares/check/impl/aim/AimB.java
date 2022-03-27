package com.gladurbad.ares.check.impl.aim;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AimCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.math.MathUtil;

@CheckInfo(name = "Aim B")
public class AimB extends AimCheck {

    public AimB(PlayerData data) {
        super(data);
    }

    private static final double MODULO_THRESHOLD = 90F;
    private static final double LINEAR_THRESHOLD = 0.1F;

    @Override
    public void handle() {
        // Get the current and last delta yaw.
        float deltaYaw = motion.getYaw();
        float lastDeltaYaw = lastMotion.getYaw();

        // Get the current and last delta pitch.
        float deltaPitch = motion.getPitch();
        float lastDeltaPitch = lastMotion.getPitch();

        // Create the divisor yaw and pitch.
        double divX = MathUtil.getGcd(deltaPitch, lastDeltaPitch);
        double divY = MathUtil.getGcd(deltaYaw, lastDeltaYaw);

        // TODO: May need to check for cinematic in certain cases.

        // Create the delta mouse X and Y.
        double deltaX = deltaYaw / divX;
        double deltaY = deltaPitch / divY;

        // Create the last delta mouse X and Y.
        double lastDeltaX = lastDeltaYaw / divX;
        double lastDeltaY = lastDeltaPitch / divY;

        // To prevent excess false positives check only when the player recently attacked.
        boolean action = attackTicks.occurred(10) || placeTicks.occurred(10);

        // To prevent excess false positive only check if the player did not mouse their mouse too fast.
        if (deltaYaw > 0.0 && deltaPitch > 0.0 && deltaYaw < 20.f && deltaPitch < 20.f && action) {
            // Create the modulo X and Y.
            double moduloX = deltaX % lastDeltaX;
            double moduloY = deltaY % lastDeltaY;

            // Get the floor delta of the the modulos
            double floorModuloX = Math.abs(Math.floor(moduloX) - moduloX);
            double floorModuloY = Math.abs(Math.floor(moduloY) - moduloY);

            // Impossible to have a different constant in two rotations
            boolean invalidX = moduloX > MODULO_THRESHOLD && floorModuloX > LINEAR_THRESHOLD;
            boolean invalidY = moduloY > MODULO_THRESHOLD && floorModuloY > LINEAR_THRESHOLD;

            if (invalidX && invalidY) {
                if (incrementBuffer() > 6) {
                    fail(
                            new Debug<>("moduloX", moduloX),
                            new Debug<>("floorModuloX", floorModuloX),
                            new Debug<>("moduloY", moduloY),
                            new Debug<>("floorModuloY", floorModuloY)
                    );
                }
            } else {
                decrementBuffer();
            }
        }
    }
}
