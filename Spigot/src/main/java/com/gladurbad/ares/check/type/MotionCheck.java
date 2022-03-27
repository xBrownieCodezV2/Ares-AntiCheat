package com.gladurbad.ares.check.type;

import com.gladurbad.ares.Ares;
import com.gladurbad.ares.check.AbstractCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.attribute.AttributeType;
import com.gladurbad.ares.util.attribute.PlayerAttributeMap;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.location.PlayerLocation;
import com.gladurbad.ares.util.motion.PlayerMotion;
import com.gladurbad.ares.util.tick.TickTimer;
import com.gladurbad.ares.util.velocity.PlayerVelocity;
import lombok.Getter;

@Getter
public abstract class MotionCheck extends AbstractCheck {

    private final boolean setback;

    public MotionCheck(PlayerData data) {
        super(data);

        setback = Ares.INSTANCE.getConfigManager()
                .getSetbackChecks()
                .contains(this.getClass().getSimpleName());
    }

    protected PlayerLocation to = data.getMotionTracker().getTo(),
            from = data.getMotionTracker().getFrom();

    protected PlayerMotion motion = data.getMotionTracker().getMotion(),
            lastMotion = data.getMotionTracker().getLastMotion();

    protected PlayerAttributeMap attributes = data.getMotionTracker().getAttributes();

    protected PlayerVelocity velocity = data.getMotionTracker().getVelocity();

    protected TickTimer
            airTicks = data.getMotionTracker().getAirTicks(),
            liquidTicks = data.getMotionTracker().getLiquidTicks(),
            slimeTicks = data.getMotionTracker().getSlimeTicks(),
            iceTicks = data.getMotionTracker().getIceTicks(),
            underBlockTicks = data.getMotionTracker().getUnderBlockTicks(),
            teleportTicks = data.getMotionTracker().getTeleportTicks(),
            halfBlockTicks = data.getMotionTracker().getHalfBlockTicks(),
            velocityTicks = data.getMotionTracker().getVelocityTicks(),
            ticksExisted = data.getMotionTracker().getTicksExisted(),
            webTicks = data.getMotionTracker().getWebTicks(),
            ladderTicks = data.getMotionTracker().getLadderTicks(),
            nonUpdateTicks = data.getMotionTracker().getNonUpdateTicks(),
            placeAffectMotionTicks = data.getActionTracker().getPlaceAffectsMotionTicks();

    public abstract void handle();

    protected void setback() {
        if (setback) {
            data.getMotionTracker().setInvalidMotion(true);
            data.getSetbackHandler().setPosition();
            data.getSetbackHandler().setSetback(true);
        }
    }

    @Override
    protected void fail(double vl, Debug<?>... debug) {
        super.fail(vl, debug);
        setback();
    }

    @Override
    protected void fail(double vl) {
        super.fail(vl);
        setback();
    }

    @Override
    protected void fail() {
        super.fail();
        setback();
    }

    @Override
    protected void fail(Debug<?>... debug) {
        super.fail(debug);
        setback();
    }

    protected boolean isFlying() {
        return attributes.get(AttributeType.ALLOWED_FLYING).get();
    }

    protected boolean wasFlying() {
        return attributes.get(AttributeType.ALLOWED_FLYING).getTicks() < 50;
    }
}
