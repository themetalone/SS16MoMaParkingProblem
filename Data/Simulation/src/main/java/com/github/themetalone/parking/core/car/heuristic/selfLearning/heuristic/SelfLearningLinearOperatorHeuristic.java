package com.github.themetalone.parking.core.car.heuristic.selfLearning.heuristic;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.AbstractMemory;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.LinearOperatorMemory;
import com.github.themetalone.parking.core.data.SimulationDataCollector;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by steff on 20.08.2016.
 */
public class SelfLearningLinearOperatorHeuristic extends SelfLearningHeuristic<List<Double>> {

    protected RealDistribution velocityDistribution;
    protected RealDistribution thresholdDistribution;

    public SelfLearningLinearOperatorHeuristic(Heuristic<List<Double>> heuristic, AbstractMemory<List<Double>> memory, double mutationRate, RealDistribution realDistribution, RealDistribution velocityDistribution, RealDistribution thresholdDistribution, SimulationDataCollector simulationDataCollector) {
        super(heuristic, memory, mutationRate, realDistribution, simulationDataCollector);
        this.velocityDistribution = velocityDistribution;
        this.thresholdDistribution = thresholdDistribution;
    }

    @Override
    protected List<Double> mutate() {
        List<Double> result = new LinkedList<>();
        result.add(velocityDistribution.sample());
        result.add(thresholdDistribution.sample());
        return result;
    }

    @Override
    protected SelfLearningHeuristic<List<Double>> makeCopy() {
        return new SelfLearningLinearOperatorHeuristic(heuristic, memory, mutationRate, realDistribution, velocityDistribution, thresholdDistribution, simulationDataCollector);
    }

    @Override
    public SelfLearningHeuristic<List<Double>> cleanCopy() {
        SelfLearningHeuristic result = new SelfLearningLinearOperatorHeuristic(heuristic, new LinearOperatorMemory(memory.getMaxSize()), mutationRate, realDistribution, velocityDistribution, thresholdDistribution, simulationDataCollector);
        result.observable = this.observable;
        this.observable.addObserver(result);
        return result;
    }
}
