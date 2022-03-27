package com.gladurbad.ares.util.nms;

import com.gladurbad.ares.nms.NmsImplementationFactory;
import com.gladurbad.ares.nms.impl.NmsImplementation;
import com.gladurbad.ares.task.type.PostTickTask;
import com.gladurbad.ares.util.boundingbox.BoundingBox;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@UtilityClass
public class NmsUtil {
    NmsImplementation nms = new NmsImplementationFactory().build();

    public Entity getEntityFromId(World world, int id) {
        return nms.getEntityFromId(world, id);
    }

    public float getBlockFriction(Location location) {
        return nms.getSlipperiness(location);
    }

    public void insertPostTickTask(PostTickTask task) {
        nms.insertPostServerTickTask(task);
    }

    public BoundingBox getEntityBb(Entity entity) {
        return nms.getEntityBb(entity);
    }

    public Vector getMotionValues(final Player player) {
        return nms.getMotionValues(player);
    }

    public void addChannel(Player player, String channel) {
        nms.addChannel(player, channel);
    }
}
