package com.gladurbad.ares.nms.impl;

import com.gladurbad.ares.task.type.PostTickTask;
import com.gladurbad.ares.util.boundingbox.BoundingBox;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public interface NmsImplementation {
    Entity getEntityFromId(World world, int id);

    float getSlipperiness(Location location);

    void insertPostServerTickTask(PostTickTask task);

    BoundingBox getEntityBb(Entity entity);

    Vector getMotionValues(Player player);

    void addChannel(Player player, String channel);
}
