package com.gladurbad.ares.data.tracker.impl;

import ac.artemis.packet.wrapper.Packet;
import ac.artemis.packet.wrapper.client.PacketPlayClientFlying;
import cc.ghast.packet.wrapper.packet.play.client.GPacketPlayClientArmAnimation;
import com.gladurbad.ares.check.type.AutoClickerCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.data.tracker.Tracker;
import com.gladurbad.ares.data.tracker.type.PacketHandler;
import com.gladurbad.ares.util.math.MathUtil;
import lombok.Getter;
import lombok.var;

import java.util.ArrayList;
import java.util.List;

public class ClickTracker extends Tracker implements PacketHandler {

    public ClickTracker(PlayerData data) {
        super(data);
    }

    private final List<Integer> samples = new ArrayList<>();

    @Getter
    private double deviation, average, kurtosis, skewness;
    @Getter
    private int outliers, distinct, duplicates, oscillation;

    private int movements;

    @Override
    public void handlePacket(Packet packet) {
        if (packet instanceof PacketPlayClientFlying) {
            // Increment the movements by 50 on the client tick, this represents the amount in milliseconds.
            movements++;
        } else if (packet instanceof GPacketPlayClientArmAnimation) {
            // Before sampling check if the player is not digging or placing and the delta is not too big.
            if (movements < 50
                    && data.getActionTracker().getDigTicks().passed(5)
                    && data.getActionTracker().getPlaceTicks().passed(5)) {
                samples.add(movements);
            }

            // Reset the movements.
            movements = 0;

            // Update the sample information at 100 samples.
            if (samples.size() == 100) {
                deviation = MathUtil.getStandardDeviation(samples);
                average = MathUtil.getAverage(samples);
                skewness = MathUtil.getSkewness(samples);
                kurtosis = MathUtil.getKurtosis(samples);

                int max = samples.stream().mapToInt(v -> v).max().orElse(0);
                int min = samples.stream().mapToInt(v -> v).min().orElse(0);

                oscillation = max - min;

                duplicates = MathUtil.getDuplicates(samples);
                distinct = samples.size() - duplicates;

                // Can't be asked to type that shit out.
                var outliersPair = MathUtil.getOutliers(samples);

                outliers = outliersPair.getKey().size() + outliersPair.getVal().size();

                // Update the autoclicker check fields and run the check.
                this.getChecks(AutoClickerCheck.class).forEach(check -> {
                    check.update();
                    check.handle();
                });

                samples.clear();
            }
        }
    }
}
