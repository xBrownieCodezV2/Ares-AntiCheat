package com.gladurbad.ares.check.impl.autoclicker;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AutoClickerCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "AutoClicker D")
public class AutoClickerD extends AutoClickerCheck {

    public AutoClickerD(PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        // Unlikely consistency at lower cps.
        if (duplicates > 75 && average > 7.0) {
            if (incrementBuffer() > 5) {
                fail(
                        new Debug<>("duplicates", duplicates),
                        new Debug<>("average", average)
                );
            }
        } else {
            decrementBuffer();
        }
    }
}
