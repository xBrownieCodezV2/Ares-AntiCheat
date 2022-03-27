package com.gladurbad.ares.check.type;

import com.gladurbad.ares.check.AbstractCheck;
import com.gladurbad.ares.data.PlayerData;

public abstract class AutoClickerCheck extends AbstractCheck {

    public AutoClickerCheck(PlayerData data) {
        super(data);
    }

    protected double deviation, average, kurtosis, skewness;
    protected int outliers, distinct, duplicates, oscillation;

    // Ugly yes, but do you really want to see deviation.get(), average.get() in your checks??? Probably would just prefer this.
    public void update() {
        deviation = data.getClickTracker().getDeviation();
        average = data.getClickTracker().getAverage();
        kurtosis = data.getClickTracker().getKurtosis();
        skewness = data.getClickTracker().getSkewness();

        outliers = data.getClickTracker().getOutliers();
        distinct = data.getClickTracker().getDistinct();
        duplicates = data.getClickTracker().getDuplicates();
        oscillation = data.getClickTracker().getOscillation();
    }

    public abstract void handle();
}
