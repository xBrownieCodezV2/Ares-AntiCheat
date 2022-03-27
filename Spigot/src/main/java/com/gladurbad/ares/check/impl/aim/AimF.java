package com.gladurbad.ares.check.impl.aim;

import com.gladurbad.ares.check.CheckInfo;
import com.gladurbad.ares.check.type.AimCheck;
import com.gladurbad.ares.data.PlayerData;
import com.gladurbad.ares.util.debug.Debug;
import com.gladurbad.ares.util.math.LinearRegression;
import com.gladurbad.ares.util.math.MathUtil;
import com.google.common.collect.Lists;
import lombok.val;

import java.util.List;

@CheckInfo(name = "Aim F")
public class AimF extends AimCheck {

    private final List<Double> samplesYaw = Lists.newArrayList();
    private final List<Double> samplesPitch = Lists.newArrayList();

    public AimF(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle() {
        /*
         * We're using the deltas from the rotations which are going to act as our mouse movements. We're
         * going to be running some statistical analysis on them to check how linear they are with each-other.
         */
        final double deltaYaw = Math.abs(to.getYaw() - from.getYaw());
        final double deltaPitch = Math.abs(to.getPitch() - from.getPitch());

        /*
         * To prevent needless flags using attacking and rate, we are going to be using the ticks from
         * before to get that time-distance in tick intervals. We need to prevent data flow to prevent falses.
         */
        final boolean attacking = attackTicks.occurred(4);

        /*
         * What we are basically trying to detect here is how linear and parallel the rotations are with each-other
         * This is done through using a data analyses algorithm called Linear-Regression which has a graph like result.
         * This is normally used for only visualizing data and not really reading but we have the option to use itt.
         */
        handle:
        {
            if (deltaYaw == 0.0 || deltaPitch == 0.0 || !attacking) break handle;

            /*
             * We're sampling the deltas inside of their corresponding samples which are going to be processed
             * when their combined total reaches 60, meaning each of the samples is going to be filled up with 30 values.
             */
            samplesYaw.add(deltaYaw);
            samplesPitch.add(deltaPitch);

            /*
             * If their combined total is 60 that means that each sample has a total of 60 values which means we are able
             * to start running our analysis. The 60 is a magic number but it's trying to be as statistically valid as
             * possible but yet still not have to have an insane amount of variables which is going to be heavy for the server.
             */
            if (samplesYaw.size() + samplesPitch.size() == 60) {
                /*
                 * The outliers are going to be used to check the amount of data that was completely external to
                 * our analysis which were normally going to false the checks. This is mostly done to prevent false flags.
                 */
                val outliersYaw = MathUtil.getOutliers(samplesYaw);
                val outliersPitch = MathUtil.getOutliers(samplesPitch);

                /*
                 * These are the array types of the regression which we need for the linear regression construction.
                 * Thankfully arrays are technically faster than array-lists so we might have a benefit here.
                 */
                Double[] regressionX = new Double[samplesYaw.size()];
                Double[] regressionY = new Double[samplesPitch.size()];

                /*
                 * To convert to a proper data array we need to parse back to the original sized array which is
                 * done through the collector system inside of the list. This is a very simple but effective system.
                 */
                regressionX = samplesYaw.toArray(regressionX);
                regressionY = samplesPitch.toArray(regressionY);

                /*
                 * This is the linear regression constructor which is going to do all the math for us to prevent us
                 * from running the same methods over and over again taking up pointless cpu cycles which matter to us.
                 */
                final LinearRegression regression = new LinearRegression(regressionX, regressionY);

                /*
                 * This is a simple counter which is going to essentially redo the outlier calculation since it is going
                 * to flag any data that is going to be way outside of the graphs expected range.
                 */
                int fails = 0;

                /*
                 * This is going to grab the next expected y value in the range in comparison to the one statistically
                 * estimated through the slope. If it way ahead of the slope that means that it was an essential outlier.
                 */
                for (int i = 0; i < 30; i++) {
                    final double tempX = regressionX[i];
                    final double tempY = regressionY[i];

                    final double predicted = regression.predict(tempX);
                    final double subtracted = predicted - tempY;

                    fails = subtracted > 0.1 ? fails + 1 : fails;
                }

                /*
                 * We're grabbing the intercepts error and the slope error which are both going to be used to estimate
                 * how inconsistent the data were in linear form which we are going to be using to calculate the error.
                 */
                final double intercepts = regression.interceptStdErr();
                final double slope = regression.slopeStdErr();

                /*
                 * We're now grabbing the pre-calculated outliers which we are going to be using to essentially filter out
                 * false flags which are forcefully caused. This would not matter on a real environment but it is good for us.
                 */
                final int outliersX = outliersYaw.getKey().size() + outliersYaw.getVal().size();
                final int outliersY = outliersPitch.getKey().size() + outliersPitch.getVal().size();

                if (intercepts > 1.4 && slope > 0.0 && fails > 15 && outliersX < 10 && outliersY < 10) {
                    if (incrementBuffer() > 1.25) {
                        this.fail(
                                new Debug<>("intercepts", intercepts),
                                new Debug<>("slope", slope)
                        );
                    }
                } else
                    this.decreaseBuffer(0.25);

                samplesYaw.clear();
                samplesPitch.clear();
            }
        }
    }
}