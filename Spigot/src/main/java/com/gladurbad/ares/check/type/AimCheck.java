package com.gladurbad.ares.check.type;

import com.gladurbad.ares.check.AbstractCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.location.PlayerLocation;
import com.gladurbad.ares.util.motion.PlayerMotion;
import com.gladurbad.ares.util.tick.TickTimer;

public abstract class AimCheck extends AbstractCheck {

    public AimCheck(PlayerData data) {
        super(data);
    }

    protected final PlayerLocation to = data.getMotionTracker().getTo(),
            from = data.getMotionTracker().getFrom();

    protected final PlayerMotion motion = data.getMotionTracker().getMotion(),
            lastMotion = data.getMotionTracker().getLastMotion();

    protected final TickTimer
            teleportTicks = data.getMotionTracker().getTeleportTicks(),
            digTicks = data.getActionTracker().getDigTicks(),
            placeTicks = data.getActionTracker().getPlaceTicks(),
            attackTicks = data.getCombatTracker().getAttackTicks();

    public abstract void handle();
}
