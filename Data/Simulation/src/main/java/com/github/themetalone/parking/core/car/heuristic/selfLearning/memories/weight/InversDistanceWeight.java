package com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.weight;

/**
 * Created by steff on 26.08.2016.
 */
public class InversDistanceWeight extends WeightUtil {

    public InversDistanceWeight() {
        WeightUtil.setInstance(this);
    }

    @Override
    public double weight(int dist) {
        if (dist < 1) {
            return 2;
        }
        return 1 / (double) dist;
    }
}
