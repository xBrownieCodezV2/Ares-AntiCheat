package com.gladurbad.ares.check.impl.autoclicker;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AutoClickerCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "AutoClicker B")
public class AutoClickerB extends AutoClickerCheck {

    public AutoClickerB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        // Random debugged value patch 2.
        if (deviation < 2.5 && skewness < 1.0 && kurtosis < -0.1 && oscillation < 5) {
            if (incrementBuffer() > 4) {
                fail(
                        new Debug<>("deviation", deviation),
                        new Debug<>("skewness", skewness),
                        new Debug<>("kurtosis", kurtosis),
                        new Debug<>("oscillation", oscillation)
                );
            }
        } else {
            decreaseBuffer(0.25);
        }
    }
}
