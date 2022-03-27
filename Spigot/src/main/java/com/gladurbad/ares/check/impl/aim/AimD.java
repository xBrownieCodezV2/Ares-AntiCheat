package com.gladurbad.ares.check.impl.aim;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AimCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.math.MathUtil;
import com.google.common.collect.Lists;
import lombok.val;

import java.util.Deque;

@CheckInfo(name = "Aim D")
public class AimD extends AimCheck {

    public AimD(PlayerData data) {
        super(data);
    }

    private final Deque<Float> samplesYaw = Lists.newLinkedList();
    private final Deque<Float> samplesPitch = Lists.newLinkedList();

    @Override
    public void handle() {
        // Get the current delta yaw and pitch.
        float deltaYaw = motion.getYaw();
        float deltaPitch = motion.getPitch();

        // To prevent falses only run this check if they recently attacked.
        boolean action = attackTicks.occurred(10);

        handle:
        {
            // Break if moving mouse too fast or not attacking.
            if (deltaYaw == 0.0 || deltaPitch == 0.0 || deltaYaw > 30.f || deltaPitch > 30.f || !action) break handle;

            // Add delta yaw and pitch to sample.
            samplesYaw.add(deltaYaw);
            samplesPitch.add(deltaPitch);

            // Check at 240 samples.
            if (samplesYaw.size() + samplesPitch.size() == 240) {
                // Get the outliers of the yaw and pitch samples.
                val outliersYaw = MathUtil.getOutliers(samplesYaw);
                val outliersPitch = MathUtil.getOutliers(samplesPitch);

                // Get the duplicates of the yaw and pitch samples.
                final double duplicatesYaw = MathUtil.getDuplicates(samplesYaw);
                final double duplicatesPitch = MathUtil.getDuplicates(samplesPitch);

                final int outliersX = outliersYaw.getKey().size() + outliersYaw.getVal().size();
                final int outliersY = outliersPitch.getKey().size() + outliersPitch.getVal().size();

                // Flag based on random debug values idk lol!
                if (duplicatesYaw < 15 && duplicatesPitch <= 9 && outliersX < 30 && outliersY < 30) {
                    if (incrementBuffer() > 3) {
                        fail(
                                new Debug<>("duplicatesYaw", duplicatesYaw),
                                new Debug<>("duplicatesPitch", duplicatesPitch),
                                new Debug<>("outliersX", outliersX),
                                new Debug<>("outliersY", outliersY)
                        );
                    }
                } else {
                    decrementBuffer();
                }

                // Clear the samples.
                samplesYaw.clear();
                samplesPitch.clear();
            }
        }
    }
}
