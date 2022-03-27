package com.gladurbad.ares.check.impl.strafe;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.nms.MathHelper;
import org.bukkit.util.Vector;

import java.util.Arrays;

@CheckInfo(name = "Strafe")
public class Strafe extends MotionCheck {
    private double motionX, motionZ;

    private static final double[] POSITIVE_FORWARD_VALUES = {0, 45, 360};
    private static final double[] NEGATIVE_FORWARD_VALUES = {135, 180};
    private static final double[] CROSS_STRAFE_VALUES = {45, 135, 90};

    public Strafe(PlayerData data) {
        super(data);
    }
    /*
     * This is one of our iteration checks which will essentially make an estimation of
     * the moveForward and the moveStrafing and it's going to use them to detect invalid key combos
     */

    public void handle() {
        final double deltaX = to.getX() - from.getX();
        final double deltaZ = to.getZ() - from.getZ();

        final double actionX = deltaX - this.motionX;
        final double actionZ = deltaZ - this.motionZ;

        float slipperiness = 0.91F;

        if (to.isOnGround()) slipperiness = to.getSlipperiness(data.getPlayer().getWorld()) * 0.91F;

        final float yaw = to.getYaw();
        final float motionYaw = (float) (Math.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) - 90.0F;

        float clamped = MathHelper.wrapAngleTo180_float(motionYaw - yaw);

        int moveForward = 0, moveStrafing = 0;

        iteration:
        {
            /*
             * We need the standardized movement and the compiled direction to be able to get the proper strafing iterations.
             * There is a single other way to be able to do this and it's extremely ugly so I am simply using a vector.
             */
            final Vector movement = new Vector(actionX, 0.0, actionZ);
            final Vector direction = new Vector(-Math.sin(Math.toRadians(yaw)), 0, Math.cos(Math.toRadians(yaw)));

            /*
             * The maximum allowed movement is generally considered 0.05. But because some clients abuse the fact that
             * it's that low, we're decreasing it to the minimum number without having issues on our part.
             */
            if (movement.length() < 0.01f) break iteration;

            /*
             * We need to normalize the vector. If we didn't the angle would be off and we would get a ridiculous
             * result with huge numbers that do not make any sense to anyone. Thus to make things easier we are normalizing it.
             */
            movement.normalize();

            /*
             * The direction is angled to the movement to get the standard angle from the movement to the head. However,
             * the result is in radians so we are compiling it to degrees through a simple division which should return degrees.
             */
            final double standard = direction.angle(movement);
            final double angle = Math.toDegrees(standard);

            if (Arrays.stream(POSITIVE_FORWARD_VALUES)
                    .anyMatch(factor -> Math.abs(angle - factor) < 0.05)) moveForward = 1;

            else if (Arrays.stream(NEGATIVE_FORWARD_VALUES)
                    .anyMatch(factor -> Math.abs(angle - factor) < 0.05)) moveForward = -1;

            if (Arrays.stream(CROSS_STRAFE_VALUES).anyMatch(factor -> Math.abs(angle - factor) < 0.05)) {
                final double product = (direction.getX() * movement.getZ()) - (direction.getZ() * movement.getX());

                moveStrafing = product > 0 ? 1 : -1;
            }
        }

        final boolean environment = to.isOnGround() && from.isOnGround() && data.getMotionTracker().isSprinting();
        final boolean invalid = (moveForward < 0) || (moveForward == 0 && moveStrafing != 0);

        if (environment && invalid)
            if (increaseBuffer(1.0) > 4) this.fail(new Debug<>("MF", moveForward), new Debug<>("MS", moveStrafing));
            else
                this.resetBuffer();

        this.motionX = deltaX * slipperiness;
        this.motionZ = deltaZ * slipperiness;

        if (Math.abs(this.motionX) < 0.005) this.motionX = 0.0;
        if (Math.abs(this.motionZ) < 0.005) this.motionZ = 0.0;
    }
}
