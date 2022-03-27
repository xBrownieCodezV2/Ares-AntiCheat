package com.gladurbad.ares.util.move;

import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.attribute.AttributeType;
import com.gladurbad.ares.util.math.MathHelper;
import com.gladurbad.ares.util.point.Point;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@UtilityClass
public class MovementUtil {
    public double[] moveFlying(double motionX, double motionZ, float strafe, float forward, float friction, float yaw) {
        float f = strafe * strafe + forward * forward;

        if (f >= 1.0E-4F) {
            f = MathHelper.sqrt(f);

            if (f < 1.0F) {
                f = 1.0F;
            }

            f = friction / f;
            strafe = strafe * f;
            forward = forward * f;
            float f1 = MathHelper.sin(yaw * (float) Math.PI / 180.0F);
            float f2 = MathHelper.cos(yaw * (float) Math.PI / 180.0F);
            motionX += strafe * f2 - forward * f1;
            motionZ += forward * f2 + strafe * f1;
        }

        return new double[]{motionX, motionZ};
    }

    public Point getLook(final float pitch, final float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Point(f1 * f2, f3, f * f2);
    }

    public double getAttributeSpeed(PlayerData data, boolean sprinting) {
        double attributeSpeed = data.getMotionTracker().getAttributes().get(AttributeType.WALK_SPEED).getLevel();

        if (sprinting)
            attributeSpeed *= 1.3F;

        final int speedAmplifier = getPotionLevel(data.getPlayer(), PotionEffectType.SPEED);

        if (speedAmplifier > 0) {
            attributeSpeed *= 1.F + (speedAmplifier * 0.2F);
        }

        return attributeSpeed;
    }

    public int getPotionLevel(Player player, PotionEffectType effect) {
        final int effectId = effect.getId();

        if (!player.hasPotionEffect(effect)) return 0;

        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType().getId() == effectId) {
                return potionEffect.getAmplifier() + 1;
            }
        }

        return 0;
    }

}
