package com.gladurbad.ares.data.setback.impl;

import cc.ghast.packet.PacketAPI;
import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerEntityVelocity;
import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.location.PlayerLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CorrectiveSetbackHandler extends AbstractSetbackHandler {
    public CorrectiveSetbackHandler(PlayerData data) {
        super(data);
    }

    private static final long SETBACK_DELAY = 250L;

    private PlayerLocation teleport;
    private GPacketPlayServerEntityVelocity velocity;
    private boolean setback;
    private long lastSetback;

    @Override
    public void tick() {
        if (teleport == null || velocity == null || !setback || System.currentTimeMillis() - lastSetback < SETBACK_DELAY) {
            return;
        }

        final PlayerLocation teleport = this.teleport;
        final GPacketPlayServerEntityVelocity velocity = this.velocity;
        this.teleport = null;
        this.velocity = null;

        Bukkit.getScheduler().runTask(Ares.INSTANCE.getPlugin(), () -> {
            final Location teleportLocation = new Location(
                    data.getPlayer().getWorld(),
                    teleport.getX(),
                    teleport.getY(),
                    teleport.getZ(),
                    data.getMotionTracker().getTo().getYaw(),
                    data.getMotionTracker().getTo().getPitch()
            );

            data.getPlayer().teleport(teleportLocation, PlayerTeleportEvent.TeleportCause.PLUGIN);
            PacketAPI.sendPacket(data.getPlayer(), velocity);
        });

        this.setback = false;
        this.lastSetback = System.currentTimeMillis();
    }

    @Override
    public void setSetback(boolean value) {
        this.setback = value;
    }

    @Override
    public void setPosition() {
        if (System.currentTimeMillis() - lastSetback > SETBACK_DELAY) {
            if (this.velocity == null)
                this.velocity = new GPacketPlayServerEntityVelocity(data.getPlayer().getEntityId(), 0, 0, 0);

            PlayerLocation lastGround = data.getMotionTracker().getLastGroundLocation();

            if (lastGround == null) lastGround = data.getMotionTracker().getTo();

            teleport = lastGround;
        }
    }

    @Override
    public long elapsedLastSetback() {
        return System.currentTimeMillis() - lastSetback;
    }
}
