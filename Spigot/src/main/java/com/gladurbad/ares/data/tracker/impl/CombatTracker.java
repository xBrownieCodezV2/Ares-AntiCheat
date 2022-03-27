package com.gladurbad.ares.data.tracker.impl;

import ac.artemis.packet.wrapper.Packet;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import cc.ghast.packet.utils.Pair;
import cc.ghast.packet.wrapper.mc.PlayerEnums;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientUseEntity;
import com.gladurbad.ares.check.type.EntityCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.tracker.Tracker;
import com.gladurbad.ares.data.tracker.type.PacketHandler;
import com.gladurbad.ares.util.boundingbox.BoundingBox;
import com.gladurbad.ares.util.evictinglist.ConcurrentEvictingList;
import com.gladurbad.ares.util.nms.NmsUtil;
import com.gladurbad.ares.util.tick.TickTimer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;


@Getter
public class CombatTracker extends Tracker implements PacketHandler {

    public CombatTracker(PlayerData data) {
        super(data);
    }

    private final TickTimer attackTicks = new TickTimer();
    private Entity entity;
    private final ConcurrentEvictingList<Pair<Integer, BoundingBox>> targetLocations = new ConcurrentEvictingList<>(40);
    @Setter
    private int cancelTicks;

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof GPacketPlayClientUseEntity) {
            GPacketPlayClientUseEntity wrapper = ((GPacketPlayClientUseEntity) packet);

            if (wrapper.getType() == PlayerEnums.UseType.ATTACK) {
                Entity entity = wrapper.getEntity();

                if (entity != null) {
                    if (this.entity != entity || !(entity instanceof Player)) {
                        targetLocations.clear();
                    }

                    this.entity = entity;
                    attackTicks.reset();
                }

                if (entity != null) {
                    this.getChecks(EntityCheck.class).forEach(EntityCheck::handle);
                }
            }
        } else if (packet instanceof PacketPlayClientFlying) {
            attackTicks.tick();
            cancelTicks = Math.max(0, cancelTicks - 1);
        }
    }

    public void tick(int tick, Entity entity) {
        targetLocations.add(new Pair<>(tick, NmsUtil.getEntityBb(entity).clone().grow(0.1f, 0.1f, 0.1f)));
    }
}
