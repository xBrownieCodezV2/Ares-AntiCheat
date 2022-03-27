package com.gladurbad.ares.check.impl.keepsprint;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import org.bukkit.entity.Player;

@CheckInfo(name = "KeepSprint")
public class KeepSprint extends MotionCheck {
    private double lastDeltaX, lastDeltaZ, lastCombined;

    public KeepSprint(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        final boolean sprinting = data.getMotionTracker().isSprinting() && to.isOnGround() && from.isOnGround();
        final boolean target = data.getCombatTracker().getEntity() != null && data.getCombatTracker().getEntity() instanceof Player;

        final boolean attacking = data.getCombatTracker().getAttackTicks().occurred(1);

        final double deltaX = to.getX() - from.getX();
        final double deltaZ = to.getZ() - from.getZ();

        handle:
        {
            if (!attacking || !target || !sprinting) break handle;

            final double estX = lastDeltaX * 0.6F;
            final double estZ = lastDeltaZ * 0.6F;

            final double offsetX = Math.abs(estX - deltaX);
            final double offsetZ = Math.abs(estZ - deltaZ);

            final double combined = offsetX + offsetZ;
            final double accel = combined - lastCombined;

            if (accel < 0.001 && Math.hypot(deltaX, deltaZ) > 0.0) {
                if (increaseBuffer(1.0) > 4)
                    this.fail(
                            new Debug<>("offsetX", offsetX),
                            new Debug<>("offsetZ", offsetZ),
                            new Debug<>("accel", accel)
                    );
            } else this.resetBuffer();

            this.lastCombined = combined;
        }

        this.lastDeltaX = deltaX;
        this.lastDeltaZ = deltaZ;
    }
}
