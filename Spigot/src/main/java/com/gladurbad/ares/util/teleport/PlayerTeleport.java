package com.gladurbad.ares.util.teleport;

import cc.ghast.packet.wrapper.packet.play.server.GPacketPlayServerPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class PlayerTeleport {
    private final double x;
    private final double y;
    private final double z;
    private final Set<GPacketPlayServerPosition.PlayerTeleportFlags> flags;

    public boolean matches(double x, double y, double z) {
        return (this.x == x || !flags.contains(GPacketPlayServerPosition.PlayerTeleportFlags.X))
                && (this.y == y || !flags.contains(GPacketPlayServerPosition.PlayerTeleportFlags.Y))
                && (this.z == z || !flags.contains(GPacketPlayServerPosition.PlayerTeleportFlags.Z));
    }
}
