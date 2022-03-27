package com.gladurbad.ares.data.tracker.impl;

import ac.artemis.packet.wrapper.Packet;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import ac.artemis.packet.wrapper.client.PacketPlayClientLook;
import ac.artemis.packet.wrapper.client.PacketPlayClientPosition;
import cc.ghast.packet.nms.EnumDirection;
import cc.ghast.packet.utils.Pair;
import cc.ghast.packet.wrapper.bukkit.BlockPosition;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientBlockPlace;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientEntityAction;
import cc.ghast.packet.wrapper.packet.play.server.*;
import com.gladurbad.ares.check.type.AimCheck;
import com.gladurbad.ares.check.type.MotionCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.tracker.Tracker;
import com.gladurbad.ares.data.tracker.type.PacketHandler;
import com.gladurbad.ares.util.attribute.AttributeType;
import com.gladurbad.ares.util.attribute.PlayerAttributeMap;
import com.gladurbad.ares.util.boundingbox.BoundingBox;
import com.gladurbad.ares.util.location.PlayerLocation;
import com.gladurbad.ares.util.material.XMaterial;
import com.gladurbad.ares.util.math.MathUtil;
import com.gladurbad.ares.util.motion.PlayerMotion;
import com.gladurbad.ares.util.move.BlockSnapshot;
import com.gladurbad.ares.util.point.Point;
import com.gladurbad.ares.util.teleport.PlayerTeleport;
import com.gladurbad.ares.util.tick.TickTimer;
import com.gladurbad.ares.util.velocity.PlayerVelocity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class MotionTracker extends Tracker implements PacketHandler {

    public MotionTracker(PlayerData data) {
        super(data);

    }

    private boolean sprinting;
    private final PlayerLocation
            to = new PlayerLocation(),
            from = new PlayerLocation();

    private PlayerLocation lastGroundLocation, lastLegitLocation;

    private final PlayerMotion
            motion = new PlayerMotion(),
            lastMotion = new PlayerMotion();

    private final PlayerVelocity velocity = new PlayerVelocity();

    private final PlayerAttributeMap attributes = new PlayerAttributeMap();

    @Getter(AccessLevel.NONE)
    private final Queue<PlayerTeleport> queuedTeleports = new LinkedList<>();

    @Getter(AccessLevel.NONE)
    @Setter
    private boolean invalidMotion;

    @Getter(AccessLevel.NONE)
    private final List<GPacketPlayClientBlockPlace> pendingBlockPlace = new ArrayList<>();

    private final TickTimer
            airTicks = new TickTimer(),
            liquidTicks = new TickTimer(),
            slimeTicks = new TickTimer(),
            iceTicks = new TickTimer(),
            underBlockTicks = new TickTimer(),
            teleportTicks = new TickTimer(),
            halfBlockTicks = new TickTimer(),
            velocityTicks = new TickTimer(),
            ticksExisted = new TickTimer(),
            ladderTicks = new TickTimer(),
            webTicks = new TickTimer(),
            nonUpdateTicks = new TickTimer();

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof PacketPlayClientFlying) {
            PacketPlayClientFlying flying = (PacketPlayClientFlying) packet;

            // Check if the flying is a movement or look packet, or both, or none.
            boolean moving = flying.isPos();
            boolean rotating = flying.isLook();

            // Grab the last position and rotation values.
            double x = from.getX();
            double y = from.getY();
            double z = from.getZ();

            float yaw = from.getYaw();
            float pitch = from.getPitch();

            // On ground status is always sent, no need to grab the last one.
            boolean onGround = flying.isOnGround();

            // Update the movement update tick timer since we need to check motion right after.
            nonUpdateTicks.tick();

            // Increase the teleport tick.
            teleportTicks.tick();

            if (moving) {
                PacketPlayClientPosition position = (PacketPlayClientPosition) flying;

                // The flying is a movement packet, set the positions to the current values.
                x = position.getX();
                y = position.getY();
                z = position.getZ();

                // Check for any responses to pending teleports.
                if (rotating && !onGround) {
                    checkTeleports(x, y, z);
                }
            } else {
                nonUpdateTicks.reset();
            }

            if (rotating) {
                PacketPlayClientLook look = (PacketPlayClientLook) flying;

                // The flying is a rotation packet, set the rotations to the current values.
                yaw = look.getYaw();
                pitch = look.getPitch();
            }

            // Update the current location with these values.
            to.set(x, y, z, yaw, pitch, onGround);

            // Update the tick timers.
            updateTickTimers();

            // Update the current collision data using the current location.
            updateCollisions();

            // Set the last ground location for setback.
            if (to.isOnGround() && airTicks.occurred(0) && MathUtil.onGround(to.getY()))
                lastGroundLocation = to.clone();

            /*
             * 1. Update the deltas since the from location won't be null.
             *
             * 2. Run the movement and rotation checks before transferring the current information to the last tick.
             * Check if both locations are set for movement since we don't need to have deltas of a million.
             *
             * 3. Check for every flying on movement, but for rotation checks only check if they are moving their head.
             */
            motion.set(to, from);

            // Set invalid motion to false.
            invalidMotion = false;

            if (ticksExisted.passed(5)) {
                this.getChecks(MotionCheck.class).forEach(MotionCheck::handle);
            }

            // Check if the player has valid motion, if they do set the last legit location to the last location.
            if (!invalidMotion) {
                lastLegitLocation = from.clone();
            }

            if (rotating) {
                this.getChecks(AimCheck.class).forEach(AimCheck::handle);
            }

            lastMotion.set(motion.clone());

            from.set(to.clone());

            // Tick the setback handler.
            data.getSetbackHandler().tick();
        } else if (packet instanceof GPacketPlayServerPosition) {
            GPacketPlayServerPosition teleport = (GPacketPlayServerPosition) packet;

            // Add the location to the queue.
            queuedTeleports.add(new PlayerTeleport(teleport.getX(), teleport.getY(), teleport.getZ(), teleport.getFlags()));
        } else if (packet instanceof GPacketPlayClientEntityAction) {
            GPacketPlayClientEntityAction action = (GPacketPlayClientEntityAction) packet;

            switch (action.getAction()) {
                case START_SPRINTING:
                    attributes.get(AttributeType.SPRINT).setLevel(1);
                    sprinting = true;
                    break;
                case STOP_SPRINTING:
                    attributes.get(AttributeType.SPRINT).setLevel(0);
                    sprinting = false;
                    break;
                case START_SNEAKING:
                    attributes.get(AttributeType.SNEAK).setLevel(1);
                    sprinting = false;
                    break;
                case STOP_SNEAKING:
                    attributes.get(AttributeType.SNEAK).setLevel(0);
                    sprinting = false;
                    break;
            }
        } else if (packet instanceof GPacketPlayServerEntityVelocity) {
            GPacketPlayServerEntityVelocity velocity = (GPacketPlayServerEntityVelocity) packet;

            // Check if the velocity is even for our data.
            if (velocity.getEntityId() == data.getPlayer().getEntityId()) {
                // Get the values.
                double velocityX = velocity.getValueX();
                double velocityY = velocity.getValueY();
                double velocityZ = velocity.getValueZ();

                // Confirm the player velocity with a transaction packet. TODO: May cause false positive with abnormal confirmation.
                data.getConnectionTracker().confirm(() -> {
                    this.velocity.set(velocityX, velocityY, velocityZ);
                    this.velocityTicks.reset();
                });
            }
        } else if (packet instanceof GPacketPlayServerEntityEffect) {
            GPacketPlayServerEntityEffect effect = (GPacketPlayServerEntityEffect) packet;

            // Check if the effect packet is for our data.
            if (effect.getEntityId() == data.getPlayer().getEntityId()) {
                Bukkit.broadcastMessage("ok effect time");
                // Get the amplifier and id of the effect.
                int amplifier = effect.getAmplifier();
                int effectId = effect.getEffectId();

                // Confirm using transaction to lag compensate.
                data.getConnectionTracker().tickAndConfirm(() -> {
                    // We only care about speed and jump boost (for now).
                    switch (effectId) {
                        case 1: // Speed effect.
                            attributes.get(AttributeType.SPEED).setLevel(amplifier);
                            attributes.reset(AttributeType.SPEED);
                            break;
                        case 8: // Jump boost effect.
                            attributes.get(AttributeType.JUMP).setLevel(amplifier);
                            attributes.reset(AttributeType.JUMP);
                            break;
                    }
                });
            }
        } else if (packet instanceof GPacketPlayServerEntityEffectRemove) {
            GPacketPlayServerEntityEffectRemove removeEffect = (GPacketPlayServerEntityEffectRemove) packet;

            // Check if the effect packet is for our data.
            if (removeEffect.getEntityId() == data.getPlayer().getEntityId()) {
                // Get the id of the effect.
                int effectId = removeEffect.getEffectId();

                // Confirm using transaction to lag compensate.
                data.getConnectionTracker().tickAndConfirm(() -> {
                    // We only care about speed and jump boost (for now).
                    switch (effectId) {
                        case 1: // Speed effect.
                            attributes.get(AttributeType.SPEED).setLevel(0);
                            attributes.reset(AttributeType.SPEED);
                            break;
                        case 8: // Jump boost effect.
                            attributes.get(AttributeType.JUMP).setLevel(0);
                            attributes.reset(AttributeType.JUMP);
                            break;
                    }
                });
            }
        } else if (packet instanceof GPacketPlayClientBlockPlace) {
            pendingBlockPlace.add((GPacketPlayClientBlockPlace) packet);
        } else if (packet instanceof GPacketPlayServerAbilities) {
            GPacketPlayServerAbilities abilities = (GPacketPlayServerAbilities) packet;

            data.getConnectionTracker().tickAndConfirm(() -> {
                if (abilities.getAllowedFlight().isPresent()) {

                    if (abilities.getAllowedFlight().get()) {
                        attributes.get(AttributeType.ALLOWED_FLYING).setLevel(1);
                    } else {
                        attributes.get(AttributeType.ALLOWED_FLYING).setLevel(0);
                    }
                    attributes.reset(AttributeType.ALLOWED_FLYING);
                }

                if (abilities.getWalkSpeed().isPresent()) {
                    attributes.get(AttributeType.WALK_SPEED).setLevel(abilities.getWalkSpeed().get());
                    attributes.reset(AttributeType.WALK_SPEED);
                }

                if (abilities.getFlySpeed().isPresent()) {
                    attributes.get(AttributeType.FLY_SPEED).setLevel(abilities.getFlySpeed().get());
                    attributes.reset(AttributeType.FLY_SPEED);
                }
            });
        }
    }

    private void updateTickTimers() {
        // Increase all ticks for the timers.
        airTicks.tick();
        slimeTicks.tick();
        iceTicks.tick();
        liquidTicks.tick();
        underBlockTicks.tick();
        halfBlockTicks.tick();
        velocityTicks.tick();
        attributes.tick();
        ticksExisted.tick();
        ladderTicks.tick();
        webTicks.tick();
    }

    private void updateCollisions() {
        // Create the bounding box from the player location.
        BoundingBox box = to.toBoundingBox(0.6f, 1.8f);
        Location location = new Location(data.getPlayer().getWorld(), to.getX(), to.getY(), to.getZ());

        // Get the players world.
        World world = data.getPlayer().getWorld();

        // Create the list of the colliding blocks.
        List<Pair<Material, Point>> blocks = box.clone()
                .grow(0.5, 0.5, 0.5)
                .move(0.0, 0.5, 0.0)
                .getColliding(world)
                .stream()
                .map(block -> new Pair<>(block.getType(), new Point(block.getX(), block.getY(), block.getZ())))
                .collect(Collectors.toList());

        for (GPacketPlayClientBlockPlace blockPlace : pendingBlockPlace) {
            Optional<EnumDirection> direction = blockPlace.getDirection();
            Optional<ItemStack> item = blockPlace.getItem();
            BlockPosition blockPosition = blockPlace.getPosition();

            if (direction.isPresent() && item.isPresent()) {
                if (item.get().getType() != Material.AIR && item.get().getType() == data.getPlayer().getItemInHand().getType()) {
                    int x = direction.get().getAdjacentX();
                    int y = direction.get().getAdjacentY();
                    int z = direction.get().getAdjacentZ();

                    Block adjacentBlock = world.getBlockAt(x, y, z);

                    if (adjacentBlock != null) {
                        boolean canPlace = adjacentBlock.getType() != Material.AIR;

                        if (canPlace) {
                            double dx = Math.abs(to.getX() - blockPosition.getX());
                            double dy = Math.abs(to.getY() - blockPosition.getY());
                            double dz = Math.abs(to.getZ() - blockPosition.getZ());

                            if (dx < 6.0 && dy < 6.0 && dz < 6.0) {
                                blocks.add(new Pair<>(item.get().getType(), new Point(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ())));
                            }
                        }
                    }
                }
            }
        }

        // Reset ticks for collisions which are true.
        for (Pair<Material, Point> pair : blocks) {
            XMaterial material = XMaterial.matchXMaterial(pair.getK());
            String name = material.name();

            if (pair.getK().isSolid()) airTicks.reset();
            if (material == XMaterial.SLIME_BLOCK) slimeTicks.reset();
            if (name.contains("ICE")) iceTicks.reset();
            if (name.contains("WATER") || name.contains("LAVA") && !name.contains("BUCKET")) liquidTicks.reset();
            if (name.contains("STAIRS") || name.contains("SLAB") || name.contains("STEP")
                    || name.contains("HEAD") || name.contains("SKULL")) halfBlockTicks.reset();
            if (material == XMaterial.COBWEB) webTicks.reset();
            if (material == XMaterial.LADDER || material == XMaterial.VINE) ladderTicks.reset();

            // TODO: Not that accurate, resets when touching wall.
            if (pair.getV().getY() >= box.minY && pair.getK().isSolid()) underBlockTicks.reset();
        }

        if (BlockSnapshot.isOnGround(location, 0) || BlockSnapshot.isOnGround(location, 1)) airTicks.reset();
        if (BlockSnapshot.isOnGround(location, -1)) underBlockTicks.reset();
        if (BlockSnapshot.isOnLiquid(location, 0) || BlockSnapshot.isOnLiquid(location, 1)) liquidTicks.reset();
        if (BlockSnapshot.isOnStairs(location, 0) || BlockSnapshot.isOnStairs(location, 1)) halfBlockTicks.reset();
        if (BlockSnapshot.isOnSlime(location, 0) || BlockSnapshot.isOnSlime(location, 1)) slimeTicks.reset();

        // Clear the block places.
        pendingBlockPlace.removeIf(packet -> System.currentTimeMillis() - packet.getTimestamp() > 500L);
    }

    private void checkTeleports(double x, double y, double z) {
        // Ensure the teleport queue isn't empty.
        if (!queuedTeleports.isEmpty()) {

            // Get the first teleport in the queue, should be the next one to be received by the client.
            PlayerTeleport teleport = queuedTeleports.peek();

            // Check the distance.
            if (teleport.matches(x, y, z)) {
                // If the distance is 0, the client has responded. Remove the teleport from the queue.
                queuedTeleports.poll();

                // Reset the teleport ticks.
                teleportTicks.reset();
            }
        }
    }
}
