package com.gladurbad.ares.data.setback;

import ac.artemis.packet.spigot.utils.factory.Factory;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.setback.impl.CorrectiveSetbackHandler;

public class SetbackHandlerFactory implements Factory<SetbackHandler> {
    private PlayerData data;

    public SetbackHandlerFactory setData(PlayerData data) {
        this.data = data;
        return this;
    }

    @Override
    public SetbackHandler build() {
        assert data != null : "Player data was not set for setback handler!";
        return new CorrectiveSetbackHandler(data);
    }
}
