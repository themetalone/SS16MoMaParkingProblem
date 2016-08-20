package com.github.themetalone.parking.core.car.heuristic.selfLearning.heuristic;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.AbstractMemory;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 * Created by steff on 19.08.2016.
 */
public class SelfLearningIntegerHeuristic extends SelfLearningHeuristic<Integer> {

    protected IntegerDistribution integerDistribution;

    public SelfLearningIntegerHeuristic(Heuristic<Integer> heuristic, AbstractMemory<Integer> memory, double mutationRate, RealDistribution realDistribution, IntegerDistribution integerDistribution) {
        super(heuristic, memory, mutationRate, realDistribution);
        this.integerDistribution = integerDistribution;
    }


    @Override
    protected Integer mutate() {
        return integerDistribution.sample();
    }

    @Override
    protected SelfLearningHeuristic<Integer> makeCopy() {
        return new SelfLearningIntegerHeuristic(heuristic, memory, mutationRate, realDistribution, integerDistribution);
    }
}
