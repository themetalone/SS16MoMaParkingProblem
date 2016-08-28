package com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.weight;

/**
 * Created by steff on 26.08.2016.
 */
public abstract class WeightUtil {

    private static WeightUtil instance = null;

    public static WeightUtil getInstance() {
        return instance;
    }

    public static void setInstance(WeightUtil instance) {
        WeightUtil.instance = instance;
    }

    public abstract double weight(int dist);

}
