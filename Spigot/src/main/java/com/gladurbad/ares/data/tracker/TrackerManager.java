package com.gladurbad.ares.data.tracker;

import ac.artemis.packet.wrapper.Packet;
import com.gladurbad.ares.check.type.PacketCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.tracker.type.EventHandler;
import com.gladurbad.ares.data.tracker.type.PacketHandler;
import com.gladurbad.ares.data.tracker.type.PostPacketHandler;
import com.gladurbad.ares.data.tracker.type.TaskHandler;
import com.gladurbad.ares.task.Task;
import com.gladurbad.ares.util.container.HeterogeneousContainer;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class TrackerManager {

    @Getter(AccessLevel.NONE)
    private final PlayerData data;
    private final HeterogeneousContainer handlerContainer = new HeterogeneousContainer();

    public TrackerManager(PlayerData data) {
        this.data = data;

        List<Tracker> trackers = new ArrayList<>(Arrays.asList(
                data.getActionTracker(),
                data.getClickTracker(),
                data.getCombatTracker(),
                data.getConnectionTracker(),
                data.getMotionTracker()
        ));

        for (Tracker tracker : trackers) {
            for (Class<?> ifc : tracker.getClass().getInterfaces()) {
                handlerContainer.put(tracker, ifc);
            }
        }
    }

    public void handlePacket(Packet packet) {
        /*
         * Each tracker has the ability to handle a packet before the checks handle the packet
         * if it implements the PacketHandler interface. Each tracker can also handle its own type of check.
         * For example, the MovementTracker can handle MovementCheck.
         */
        handlerContainer.get(PacketHandler.class).forEach(v -> v.handlePacket(packet));


        /*
         * After each type of check is called from the tracker, the last check type to be handled is PacketCheck.
         * Here, each packet will be passed to these checks, so you can do anything here.
         */
        data.getCheckData().getCategorizedChecks().get(PacketCheck.class).forEach(v -> v.handle(packet));

        /*
         * After the checks handle the packet, any tracker implementing the PostPacketHandler interface can
         * handle the packet after the check. This is useful if you want to update something after a check runs,
         * for example, interpolation of a reach check.
         */
        handlerContainer.get(PostPacketHandler.class).forEach(v -> v.handlePostPacket(packet));
    }

    public void handleEvent(Event event) {
        handlerContainer.get(EventHandler.class).forEach(handler -> handler.handleBukkitEvent(event));
    }

    public void handleTask(Task task) {
        handlerContainer.get(TaskHandler.class).forEach(handler -> handler.handleTask(task));
    }
}
