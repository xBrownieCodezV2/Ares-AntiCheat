package com.gladurbad.ares.check.impl.aim;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AimCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.math.MathUtil;
import com.google.common.collect.Lists;
import lombok.val;

import java.util.Deque;

@CheckInfo(name = "Aim C")
public class AimC extends AimCheck {

    public AimC(PlayerData data) {
        super(data);
    }

    private final Deque<Float> samplesYaw = Lists.newLinkedList();
    private final Deque<Float> samplesPitch = Lists.newLinkedList();

    private float lastDifferenceYaw, lastDifferencePitch;

    @Override
    public void handle() {
        // Get the current and last delta yaw.
        float deltaYaw = motion.getYaw();
        float lastDeltaYaw = lastMotion.getYaw();

        // Get the current and last delta pitch.
        float deltaPitch = motion.getPitch();
        float lastDeltaPitch = lastMotion.getPitch();

        // Create the acceleration yaw and pitch.
        float differenceYaw = Math.abs(deltaYaw - lastDeltaYaw);
        float differencePitch = Math.abs(deltaPitch - lastDeltaPitch);

        // Create the jolt yaw and pitch, basically acceleration over time.
        float joltX = Math.abs(differenceYaw - lastDifferenceYaw);
        float joltY = Math.abs(differencePitch - lastDifferencePitch);

        boolean action = attackTicks.occurred(10);

        handle:
        {
            if (joltX == 0.0 || joltY == 0.0 || !action) break handle;

            samplesYaw.add(joltX);
            samplesPitch.add(joltY);

            if (samplesYaw.size() + samplesPitch.size() == 60) {
                val outliersYaw = MathUtil.getOutliers(samplesYaw);
                val outliersPitch = MathUtil.getOutliers(samplesPitch);

                final int duplicatesX = MathUtil.getDuplicates(samplesYaw);
                final int duplicatesY = MathUtil.getDuplicates(samplesPitch);

                final int outliersX = outliersYaw.getKey().size() + outliersYaw.getVal().size();
                final int outliersY = outliersPitch.getKey().size() + outliersPitch.getVal().size();

                if (duplicatesX + duplicatesY == 0.0 && outliersX < 5 && outliersY < 5) {
                    if (incrementBuffer() > 1) {
                        fail(
                                new Debug<>("outliersX", outliersX),
                                new Debug<>("outliersY", outliersY)
                        );
                    }
                } else {
                    decreaseBuffer(0.5);
                }

                samplesYaw.clear();
                samplesPitch.clear();
            }
        }

        lastDifferenceYaw = differenceYaw;
        lastDifferencePitch = differencePitch;
    }
}
