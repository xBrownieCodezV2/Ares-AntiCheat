package com.gladurbad.ares.check.type;

import cc.ghast.packet.utils.Pair;
import com.gladurbad.ares.Ares;
import com.gladurbad.ares.check.AbstractCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.boundingbox.BoundingBox;
import com.gladurbad.ares.util.evictinglist.ConcurrentEvictingList;
import com.gladurbad.ares.util.location.PlayerLocation;
import com.gladurbad.ares.util.math.MathUtil;
import com.gladurbad.ares.util.motion.PlayerMotion;
import com.gladurbad.ares.util.tick.TickTimer;
import org.bukkit.entity.Entity;

import java.util.stream.Stream;

public abstract class EntityCheck extends AbstractCheck {

    public EntityCheck(PlayerData data) {
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
            attackTicks = data.getCombatTracker().getAttackTicks(),
            lagTicks = data.getConnectionTracker().getLagTick();

    protected final Entity entity = data.getCombatTracker().getEntity();

    protected final ConcurrentEvictingList<Pair<Integer, BoundingBox>> targetLocations = data.getCombatTracker().getTargetLocations();

    public abstract void handle();

    protected Stream<BoundingBox> filterLocations(long pingTicks, int leniency) {
        int serverTick = Ares.INSTANCE.getTaskManager().getTick();

        return targetLocations.stream()
                .filter(pair -> Math.abs(serverTick - pair.getK() - pingTicks) < leniency)
                .map(Pair::getV);
    }

    protected long getPingTicks() {
        return MathUtil.getPingTicks(data.getConnectionTracker().getPing(), 3);
    }
}
