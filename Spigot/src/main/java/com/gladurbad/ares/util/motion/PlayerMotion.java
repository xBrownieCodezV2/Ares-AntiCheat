package com.gladurbad.ares.util.motion;

import com.gladurbad.ares.util.location.PlayerLocation;
import com.gladurbad.ares.util.math.MathUtil;
import lombok.Getter;

@Getter
public class PlayerMotion {

    private PlayerLocation to, from;
    private double x, y, z, horizontalDistance;
    private float yaw, pitch;

    public PlayerMotion(PlayerLocation to, PlayerLocation from) {
        this.to = to;
        this.from = from;

        this.x = to.x - from.x;
        this.y = to.y - from.y;
        this.z = to.z - from.z;

        this.horizontalDistance = MathUtil.hypot(x, z);

        this.yaw = Math.abs(to.getYaw() - from.getYaw());
        this.pitch = Math.abs(to.getPitch() - from.getPitch());
    }

    public void set(PlayerLocation to, PlayerLocation from) {
        this.to = to;
        this.from = from;

        this.x = to.x - from.x;
        this.y = to.y - from.y;
        this.z = to.z - from.z;

        this.horizontalDistance = MathUtil.hypot(x, z);

        this.yaw = Math.abs(to.getYaw() - from.getYaw());
        this.pitch = Math.abs(to.getPitch() - from.getPitch());
    }

    public void set(PlayerMotion motion) {
        this.to = motion.getTo();
        this.from = motion.getFrom();

        this.x = motion.getTo().getX() - motion.getFrom().getX();
        this.y = motion.getTo().getY() - motion.getFrom().getY();
        this.z = motion.getTo().getZ() - motion.getFrom().getZ();

        this.horizontalDistance = MathUtil.hypot(motion.getX(), motion.getZ());

        this.yaw = Math.abs(motion.getTo().getYaw() - motion.getFrom().getYaw());
        this.pitch = Math.abs(motion.getTo().getPitch() - motion.getFrom().getPitch());
    }

    public PlayerMotion() {
        this.to = null;
        this.from = null;

        this.x = 0;
        this.y = 0;
        this.z = 0;

        this.horizontalDistance = 0;

        this.yaw = 0;
        this.pitch = 0;
    }

    public PlayerMotion clone() {
        return new PlayerMotion(to, from);
    }
}
