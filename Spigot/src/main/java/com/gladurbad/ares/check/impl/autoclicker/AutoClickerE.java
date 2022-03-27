package com.gladurbad.ares.check.impl.autoclicker;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AutoClickerCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "AutoClicker E")
public class AutoClickerE extends AutoClickerCheck {

    public AutoClickerE(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        // Unlikely consistency at lower cps.
        if (outliers < 20 && average > 7.0 && deviation < 6.0) {
            if (incrementBuffer() > 5) {
                fail(
                        new Debug<>("outliers", outliers),
                        new Debug<>("average", average),
                        new Debug<>("deviation", deviation)
                );
            }
        } else {
            decrementBuffer();
        }
    }
}
