package com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.weight;

/**
 * Created by steff on 26.08.2016.
 */
public class QuarterWeight extends WeightUtil {

    double bestQuarterWeight = 8;
    double bestHalfWeight = 4;
    double worstHalfWeight = 2;
    double worstQuarterWeight = 1;
    double streetLength = 1;

    public QuarterWeight(double bestQuarterWeight, double bestHalfWeight, double worstHalfWeight, double worstQuarterWeight, double streetLength) {
        this.bestQuarterWeight = bestQuarterWeight;
        this.bestHalfWeight = bestHalfWeight;
        this.worstHalfWeight = worstHalfWeight;
        this.worstQuarterWeight = worstQuarterWeight;
        this.streetLength = streetLength;
        WeightUtil.setInstance(this);
    }

    public double weight(int dist) {
        double distanceRatio = dist / streetLength;
        if (distanceRatio < 0.25) {
            return bestQuarterWeight;
        }
        if (distanceRatio < 0.5) {
            return bestHalfWeight;
        }
        if (distanceRatio < 0.75) {
            return worstHalfWeight;
        }
        return worstQuarterWeight;
    }
}
