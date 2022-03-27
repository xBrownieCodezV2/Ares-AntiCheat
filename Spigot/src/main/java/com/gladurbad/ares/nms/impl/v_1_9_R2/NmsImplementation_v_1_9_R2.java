package com.gladurbad.ares.nms.impl.v_1_9_R2;

import com.gladurbad.ares.nms.impl.NmsImplementation;
import com.gladurbad.ares.task.type.PostTickTask;
import com.gladurbad.ares.util.boundingbox.BoundingBox;
import net.minecraft.server.v1_9_R2.AxisAlignedBB;
import net.minecraft.server.v1_9_R2.BlockPosition;
import net.minecraft.server.v1_9_R2.EntityPlayer;
import net.minecraft.server.v1_9_R2.MinecraftServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class NmsImplementation_v_1_9_R2 implements NmsImplementation {
    @Override
    public Entity getEntityFromId(World world, int id) {
        CraftWorld craftWorld = (CraftWorld) world;
        net.minecraft.server.v1_9_R2.World nmsWorld = craftWorld.getHandle();
        net.minecraft.server.v1_9_R2.Entity nmsEntity = nmsWorld.getEntity(id);

        return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
    }

    @Override
    public float getSlipperiness(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        BlockPosition blockPos = new BlockPosition(x, y, z);
        net.minecraft.server.v1_9_R2.Block nmsBlock = MinecraftServer.getServer().getWorld().getType(blockPos).getBlock();

        return nmsBlock.frictionFactor;
    }

    @Override
    public void insertPostServerTickTask(PostTickTask task) {
        MinecraftServer.getServer().a(task::handlePostTick);
    }

    @Override
    public BoundingBox getEntityBb(Entity entity) {
        AxisAlignedBB aabb = ((CraftEntity) entity).getHandle().getBoundingBox();

        return new BoundingBox(aabb.a, aabb.b, aabb.c, aabb.d, aabb.e, aabb.f);
    }

    @Override
    public Vector getMotionValues(Player player) {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

        double motionX = entityPlayer.motX;
        double motionY = entityPlayer.motY;
        double motionZ = entityPlayer.motZ;


        return new Vector(motionX, motionY, motionZ);
    }

    @Override
    public void addChannel(Player player, String channel) {
        ((CraftPlayer) player).addChannel(channel);
    }
}
