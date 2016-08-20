package com.github.themetalone.parking.core.car.heuristic.selfLearning.memories;

import com.github.themetalone.parking.core.car.heuristic.selfLearning.Parameter;

/**
 * Created by steff on 19.08.2016.
 */
public class IntegerMemory extends AbstractMemory<Integer> {

    int bestQuarterWeight = 8;
    int bestHalfWeight = 4;
    int worstHalfWeight = 2;
    int worstQuarterWeight = 1;

    public IntegerMemory(int initialCapacity) {
        super(initialCapacity);
    }

    public Integer learnedParameter() {
        int weightedParamSum = super.stream()
                .sorted((p1, p2) -> p1.getDistance() - p2.getDistance())
                .mapToInt((p) -> weight(p.getDistance()) * p.getParam())
                .sum();
        int totalWeight = super.stream()
                .mapToInt(Parameter::getDistance)
                .map(this::weight)
                .sum();
        return weightedParamSum / totalWeight;
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

    public void setStreetLength(int streetLength) {
        this.streetLength = streetLength;
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