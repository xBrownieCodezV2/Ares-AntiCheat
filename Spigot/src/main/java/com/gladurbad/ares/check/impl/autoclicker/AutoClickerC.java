package com.gladurbad.ares.check.impl.autoclicker;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AutoClickerCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;

@CheckInfo(name = "AutoClicker C")
public class AutoClickerC extends AutoClickerCheck {

    public AutoClickerC(PlayerData data) {
        super(data);
    }

    private double lastSkewness, lastKurtosis, lastDeviation;
    private boolean set;

    @Override
    public void handle() {
        double deltaSkewness = Math.abs(skewness - lastSkewness);
        double deltaKurtosis = Math.abs(kurtosis - lastKurtosis);
        double deltaDeviation = Math.abs(deviation - lastDeviation);

        // Similar statistics may indicate use of a macro or poor autoclicker randomization.
        if (set && deltaSkewness < 0.1 && deltaKurtosis < 0.1 && deltaDeviation < 0.1) {
            if (incrementBuffer() > 3) {
                fail(
                        new Debug<>("deltaSkewness", deltaSkewness),
                        new Debug<>("deltaKurtosis", deltaKurtosis),
                        new Debug<>("deltaDeviation", deltaDeviation)
                );
            }
        } else {
            decrementBuffer();
        }

        lastSkewness = skewness;
        lastKurtosis = kurtosis;
        lastDeviation = deviation;

        set = true;
    }
}
