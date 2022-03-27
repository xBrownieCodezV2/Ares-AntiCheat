package com.gladurbad.ares.packet;

import ac.artemis.packet.PacketListener;
import ac.artemis.packet.profile.Profile;
import ac.artemis.packet.wrapper.Packet;
import cc.ghast.packet.PacketAPI;
import com.gladurbad.ares.Ares;
import com.gladurbad.ares.data.PlayerData;

public class PacketParser implements PacketListener {

    public PacketParser() {
        PacketAPI.addListener(this);
    }

    @Override
    public void onPacket(Profile profile, Packet packet) {
        if (Ares.INSTANCE.isEnabled()) {
            PlayerData data = Ares.INSTANCE.getPlayerDataManager().get(profile.getUuid());

            if (data != null) {
                data.getService().getExecutor().execute(() ->
                        data.getTrackerManager().handlePacket(packet));

            }
        }
    }
}
