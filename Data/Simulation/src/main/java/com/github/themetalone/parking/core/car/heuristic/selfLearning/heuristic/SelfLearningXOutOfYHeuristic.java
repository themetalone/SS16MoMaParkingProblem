package com.github.themetalone.parking.core.car.heuristic.selfLearning.heuristic;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.AbstractMemory;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.XOutOfYMemory;
import com.github.themetalone.parking.core.data.SimulationDataCollector;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by steff on 20.08.2016.
 */
public class SelfLearningXOutOfYHeuristic extends SelfLearningHeuristic<List<Integer>> {

    protected RealDistribution xRealDistribution;
    protected IntegerDistribution yIntegerDistribution;

    public SelfLearningXOutOfYHeuristic(Heuristic<List<Integer>> heuristic, AbstractMemory<List<Integer>> memory, double mutationRate, RealDistribution realDistribution, RealDistribution xRealDistribution, IntegerDistribution yIntegerDistribution, SimulationDataCollector simulationDataCollector) {
        super(heuristic, memory, mutationRate, realDistribution, simulationDataCollector);
        this.xRealDistribution = xRealDistribution;
        this.yIntegerDistribution = yIntegerDistribution;
    }

    @Override
    protected List<Integer> mutate() {

        int y = yIntegerDistribution.sample();
        double xByY = xRealDistribution.sample();
        int x = (int) (xByY * y);
        List<Integer> result = new LinkedList<>();
        result.add(x);
        result.add(y);
        return result;
    }

    @Override
    protected SelfLearningHeuristic<List<Integer>> makeCopy() {
        return new SelfLearningXOutOfYHeuristic(heuristic, memory, mutationRate, realDistribution, xRealDistribution, yIntegerDistribution, simulationDataCollector);
    }

    @Override
    public SelfLearningHeuristic<List<Integer>> cleanCopy() {
        SelfLearningHeuristic result = new SelfLearningXOutOfYHeuristic(heuristic, new XOutOfYMemory(memory.getMaxSize()), mutationRate, realDistribution, xRealDistribution, yIntegerDistribution, simulationDataCollector);
        result.observable = this.observable;
        this.observable.addObserver(result);
        return result;
    }
}
