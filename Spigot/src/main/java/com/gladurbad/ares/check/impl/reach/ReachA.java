package com.gladurbad.ares.check.impl.reach;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.EntityCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.location.PlayerLocation;

@CheckInfo(name = "Reach A")
public class ReachA extends EntityCheck {

    public ReachA(PlayerData data) {
        super(data);
    }

    private static final int FILTER_LENIENCY = 3;
    private static final double THRESHOLD = 3.0;
    private static final double LAG_LENIENCY = 1.0;

    @Override
    public void handle() {
        // Get the player pitch, we factor this into the distance calculation for higher accuracy.
        float pitch = to.getPitch();

        /*
         * Get the current location. The correct location for a reach calculation should be the last location,
         * however since this check is handled on the use entity packet, which is sent before the flying packet,
         * the current location will work perfectly in this case.
         */
        PlayerLocation location = to.clone();

        // Check if the player is lagging within 15 ticks.
        boolean lagging = lagTicks.occurred(15);

        // Get the latency of the player represented within server ticks, adding a leniency.
        long pingTicks = this.getPingTicks();

        /*
         * Filter the tracked entity locations based on the real-time transaction ping. During lag, this can be
         * somewhat inaccurate, so we will add a leniency of 1.0 extra reach if they have lagged in the last 15 ticks.
         *
         * After getting the filtered locations, get the minimum distance using the bounding box distance, factoring
         * in the pitch using basic trigonometry for a more accurate result. This check won't be extremely accurate,
         * in this case we are expecting a regular detection of 3.15 reach with at least 4.0 block reach at extreme lag.
         *
         * TODO: Make the thresholds configurable.
         */

        double reach = filterLocations(pingTicks, FILTER_LENIENCY)
                .mapToDouble(bb -> bb.getEyeDistance(pitch, location))
                .min()
                .orElse(-1.0);

        if (reach == -1.0) return;

        double threshold = THRESHOLD + (lagging ? LAG_LENIENCY : 0);

        if (reach > threshold) {
            double ratio = reach / threshold;

            if (increaseBuffer(ratio) > 2) {
                fail(ratio, new Debug<>("reach", reach), new Debug<>("limit", threshold));
                cancelCombat(20);
            }
        } else {
            decreaseBuffer(0.005);
        }
    }
}
