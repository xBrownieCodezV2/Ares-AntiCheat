package com.gladurbad.ares.nms.impl.v_1_10_R1;

import com.gladurbad.ares.nms.impl.NmsImplementation;
import com.gladurbad.ares.task.type.PostTickTask;
import com.gladurbad.ares.util.boundingbox.BoundingBox;
import net.minecraft.server.v1_10_R1.AxisAlignedBB;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.MinecraftServer;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class NmsImplementation_v_1_10_R1 implements NmsImplementation {
    @Override
    public Entity getEntityFromId(World world, int id) {
        CraftWorld craftWorld = (CraftWorld) world;
        net.minecraft.server.v1_10_R1.World nmsWorld = craftWorld.getHandle();
        net.minecraft.server.v1_10_R1.Entity nmsEntity = nmsWorld.getEntity(id);

        return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
    }

    @Override
    public float getSlipperiness(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        BlockPosition blockPos = new BlockPosition(x, y, z);
        net.minecraft.server.v1_10_R1.Block nmsBlock = MinecraftServer.getServer().getWorld().getType(blockPos).getBlock();

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
