package com.github.themetalone.parking.core.car.heuristic.selfLearning.memories;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by steff on 20.08.2016.
 */
public class LinearOperatorMemory extends AbstractMemory<List<Double>> {

    int bestQuarterWeight = 8;
    int bestHalfWeight = 4;
    int worstHalfWeight = 2;
    int worstQuarterWeight = 1;

    public LinearOperatorMemory(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public List<Double> learnedParameter() {
        List<Double> result = new LinkedList<>();
        double weightedVelocity = super.stream().mapToDouble(p -> p.getParam().get(0) * weight(p.getDistance())).sum();
        double weightedThreshold = super.stream().mapToDouble(p -> p.getParam().get(1) * weight(p.getDistance())).sum();
        int totalWeight = super.stream().mapToInt(p -> weight(p.getDistance())).sum();
        result.add(weightedVelocity / totalWeight);
        result.add(weightedThreshold / totalWeight);
        return result;
    }

    @SuppressWarnings("Duplicates")
    private int weight(int dist) {
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

    public void setBestQuarterWeight(int bestQuarterWeight) {
        this.bestQuarterWeight = bestQuarterWeight;
    }

    public void setBestHalfWeight(int bestHalfWeight) {
        this.bestHalfWeight = bestHalfWeight;
    }

    public void setWorstHalfWeight(int worstHalfWeight) {
        this.worstHalfWeight = worstHalfWeight;
    }

    public void setWorstQuarterWeight(int worstQuarterWeight) {
        this.worstQuarterWeight = worstQuarterWeight;
    }


}
