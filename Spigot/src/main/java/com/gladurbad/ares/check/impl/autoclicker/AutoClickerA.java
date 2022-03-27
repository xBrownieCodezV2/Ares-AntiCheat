package com.gladurbad.ares.check.impl.autoclicker;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AutoClickerCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "AutoClicker A")
public class AutoClickerA extends AutoClickerCheck {

    public AutoClickerA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        // Random debugged value patch.
        if (deviation < 0.95 && average > 7.1F && oscillation < 7) {
            if (incrementBuffer() > 4) {
                fail(
                        new Debug<>("deviation", deviation),
                        new Debug<>("average", average),
                        new Debug<>("oscillation", oscillation)
                );
            }
        } else {
            decreaseBuffer(0.25);
        }
    }
}
