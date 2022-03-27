package com.gladurbad.ares.check.impl.velocity;

import ac.artemis.packet.protocol.ProtocolVersion;
import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.move.MovementUtil;
import com.gladurbad.ares.util.nms.MathHelper;
import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.entity.Player;

@CheckInfo(name = "Velocity B")
public class VelocityB extends MotionCheck {

    public VelocityB(PlayerData data) {
        super(data);
    }

    private float slipperiness;

    @Override
    public void handle() {
        if (velocityTicks.occurred(1)) {
            if (teleportTicks.occurred(1)
                    || liquidTicks.occurred(5)
                    || ladderTicks.occurred(5)
                    || halfBlockTicks.occurred(5)
                    || slimeTicks.occurred(5)
                    || data.getPlayer().isInsideVehicle())
                return;

            float yaw = to.getYaw();

            double velocityX = velocity.getX();
            double velocityZ = velocity.getZ();

            double motionX = motion.getX();
            double motionZ = motion.getZ();

            AtomicDouble min = new AtomicDouble(Double.MAX_VALUE);

            for (int mf = -1; mf < 2; mf++) {
                for (int ms = -1; ms < 2; ms++) {
                    for (int j = 0; j < 2; j++) {
                        for (int g = 0; g < 2; g++) {
                            for (int sp = 0; sp < 2; sp++) {
                                for (int at = 0; at < 2; at++) {
                                    double copyVelocityX = velocityX;
                                    double copyVelocityZ = velocityZ;

                                    double threshold = data.getVersion().isBelow(ProtocolVersion.V1_9) ? 0.005D : 0.003D;

                                    if (Math.abs(copyVelocityX) < threshold) copyVelocityX = 0.0D;
                                    if (Math.abs(copyVelocityZ) < threshold) copyVelocityZ = 0.0D;

                                    float moveForward = mf * 0.98F;
                                    float moveStrafe = ms * 0.98F;

                                    boolean jumping = j == 1;
                                    boolean onGround = g == 1;
                                    boolean sprinting = sp == 1;
                                    boolean attack = at == 1;

                                    if (attack && data.getCombatTracker().getAttackTicks().occurred(5)
                                            && data.getCombatTracker().getEntity() instanceof Player) {
                                        copyVelocityX *= 0.6D;
                                        copyVelocityZ *= 0.6D;
                                    }

                                    if (jumping && sprinting) {
                                        float yawRadians = yaw * 0.017453292F;
                                        copyVelocityX -= MathHelper.sin(yawRadians) * 0.2F;
                                        copyVelocityZ += MathHelper.cos(yawRadians) * 0.2F;
                                    }

                                    float f4 = 0.91F;

                                    if (onGround) {
                                        f4 = slipperiness * 0.91F;
                                    }

                                    float f = 0.16277136F / (f4 * f4 * f4);
                                    float f5;

                                    if (onGround) {
                                        f5 = (float) (MovementUtil.getAttributeSpeed(data, sprinting) * f);
                                    } else {
                                        f5 = sprinting ? 0.026F : 0.02F;
                                    }

                                    double[] moveFlyingResult = MovementUtil.moveFlying(copyVelocityX, copyVelocityZ, moveStrafe, moveForward, f5, yaw);

                                    copyVelocityX = moveFlyingResult[0];
                                    copyVelocityZ = moveFlyingResult[1];

                                    double discrepancyX = motionX - copyVelocityX;
                                    double discrepancyZ = motionZ - copyVelocityZ;

                                    double offset = discrepancyX * discrepancyX + discrepancyZ * discrepancyZ;

                                    if (offset < min.get()) min.set(offset);
                                }
                            }
                        }
                    }
                }
            }

            // 0.03 is a bitch.
            double threshold = nonUpdateTicks.occurred(3) ? 3e-4 : 1e-7;

            if (min.get() > threshold) {
                if (incrementBuffer() > 5) {
                    fail(
                            new Debug<>("offset", min.get()),
                            new Debug<>("threshold", threshold),
                            new Debug<>("nonUpdateTicks", nonUpdateTicks.getTicks())
                    );
                }
            } else {
                decreaseBuffer(0.05);
            }

            slipperiness = from.getSlipperiness(data.getPlayer().getWorld());
        }
    }
}
