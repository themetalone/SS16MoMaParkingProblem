package com.github.themetalone.parking.core.car.heuristic.selfLearning.heuristic;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.AbstractMemory;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.IntegerMemory;
import com.github.themetalone.parking.core.data.SimulationDataCollector;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 * Created by steff on 19.08.2016.
 */
public class SelfLearningIntegerHeuristic extends SelfLearningHeuristic<Integer> {

    protected IntegerDistribution integerDistribution;

    public SelfLearningIntegerHeuristic(Heuristic<Integer> heuristic, AbstractMemory<Integer> memory, double mutationRate, RealDistribution realDistribution, IntegerDistribution integerDistribution, SimulationDataCollector simulationDataCollector) {
        super(heuristic, memory, mutationRate, realDistribution, simulationDataCollector);
        this.integerDistribution = integerDistribution;

    }


    @Override
    protected Integer mutate() {
        return integerDistribution.sample();
    }

    @Override
    protected SelfLearningHeuristic<Integer> makeCopy() {
        return new SelfLearningIntegerHeuristic(heuristic, memory, mutationRate, realDistribution, integerDistribution, simulationDataCollector);
    }

    @Override
    public SelfLearningHeuristic<Integer> cleanCopy() {
        SelfLearningHeuristic result =  new SelfLearningIntegerHeuristic(heuristic, new IntegerMemory(memory.getMaxSize()), mutationRate, realDistribution, integerDistribution, simulationDataCollector);
        result.observable = this.observable;
        this.observable.addObserver(result);
        return result;
    }
}
