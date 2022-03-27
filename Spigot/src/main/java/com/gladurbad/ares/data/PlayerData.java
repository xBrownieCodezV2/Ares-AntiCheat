package com.gladurbad.ares.data;

import ac.artemis.packet.protocol.ProtocolVersion;
import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.check.CheckData;
import com.gladurbad.ares.data.command.CommandManager;
import com.gladurbad.ares.data.setback.SetbackHandler;
import com.gladurbad.ares.data.setback.SetbackHandlerFactory;
import com.gladurbad.ares.data.tracker.TrackerManager;
import com.gladurbad.ares.data.tracker.impl.*;
import com.gladurbad.ares.util.string.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class PlayerData {

    private final Player player;
    private final ProtocolVersion version;
    private final String versionString;
    private final long joinTime;

    @Setter
    private PlayerDataChunk service;

    private final TrackerManager trackerManager;
    private final CommandManager commandManager;

    private final ActionTracker actionTracker;
    private final ClickTracker clickTracker;
    private final CombatTracker combatTracker;
    private final ConnectionTracker connectionTracker;
    private final MotionTracker motionTracker;

    private final SetbackHandler setbackHandler;
    private final CheckData checkData;

    @Setter
    private String clientBrand;
    @Setter
    private boolean banning;

    public PlayerData(Player player) {
        this.player = player;
        this.joinTime = System.currentTimeMillis();

        this.version = /*PacketAPI.getVersion(player.getUniqueId())*/ ProtocolVersion.UNKNOWN;
        this.versionString = version.toString()
                .replace("_", ".")
                .replace("V", "");

        actionTracker = new ActionTracker(this);
        clickTracker = new ClickTracker(this);
        combatTracker = new CombatTracker(this);
        connectionTracker = new ConnectionTracker(this);
        motionTracker = new MotionTracker(this);

        trackerManager = new TrackerManager(this);
        commandManager = new CommandManager(this);

        setbackHandler = new SetbackHandlerFactory().setData(this).build();

        checkData = new CheckData(this);
    }

    public void message(String... messages) {
        for (String message : messages) {
            player.sendMessage(StringUtil.color(message));
        }
    }

    public void message(List<String> messages) {
        for (String message : messages) {
            player.sendMessage(StringUtil.color(message));
        }
    }


    public void monkey(String reason) {
        if (!isBanning()) {
            setBanning(true);

            Bukkit.getScheduler().runTask(Ares.INSTANCE.getPlugin(), () -> {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + player.getName() + " " + reason);
                setBanning(false);
            });
        }
    }
}
