package com.gladurbad.ares.data.tracker.impl;

import ac.artemis.packet.wrapper.Packet;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import cc.ghast.packet.nms.EnumDirection;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientBlockDig;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientBlockPlace;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.tracker.Tracker;
import com.gladurbad.ares.data.tracker.type.EventHandler;
import com.gladurbad.ares.data.tracker.type.PacketHandler;
import com.gladurbad.ares.util.math.MathUtil;
import com.gladurbad.ares.util.tick.TickTimer;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@Getter
public class ActionTracker extends Tracker implements PacketHandler, EventHandler {

    public ActionTracker(PlayerData data) {
        super(data);
    }

    private final TickTimer
            digTicks = new TickTimer(),
            placeTicks = new TickTimer(),
            placeAffectsMotionTicks = new TickTimer();

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof GPacketPlayClientBlockDig) {
            digTicks.reset();
        } else if (packet instanceof GPacketPlayClientBlockPlace) {
            placeTicks.reset();

            GPacketPlayClientBlockPlace wrapper = (GPacketPlayClientBlockPlace) packet;

            // Shitty fix sort of.
            if (wrapper.getPosition().getY() < data.getMotionTracker().getTo().getY()
                    && data.getPlayer().getItemInHand().getType().isSolid()
                    && wrapper.getItem().isPresent()) {
                if (wrapper.getDirection().isPresent() && wrapper.getDirection().get() == EnumDirection.UP) {
                    double dx = Math.abs(data.getMotionTracker().getTo().getX() - wrapper.getPosition().getX());
                    double dz = Math.abs(data.getMotionTracker().getTo().getZ() - wrapper.getPosition().getZ());

                    if (MathUtil.hypot(dx, dz) < 1.5) {
                        placeAffectsMotionTicks.reset();
                    }
                }
            }
        } else if (packet instanceof PacketPlayClientFlying) {
            placeTicks.tick();
            digTicks.tick();
            placeAffectsMotionTicks.tick();
        }
    }

    @Override
    public void handleBukkitEvent(Event event) {
        if (event instanceof PlayerInteractEvent) {
            PlayerInteractEvent interact = (PlayerInteractEvent) event;

            if (interact.getAction() == Action.LEFT_CLICK_BLOCK) {
                digTicks.reset();
            }
        }
    }
}
