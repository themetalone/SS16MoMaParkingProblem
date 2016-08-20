package com.github.themetalone.parking.core.car.heuristic.selfLearning.heuristic;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.Parameter;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.AbstractMemory;
import com.github.themetalone.parking.core.slot.ParkingSlot;
import org.apache.commons.math3.distribution.RealDistribution;

/**
 * Created by steff on 18.08.2016.
 */
public abstract class SelfLearningHeuristic<T> implements Heuristic<T> {

    protected Heuristic<T> heuristic;
    protected AbstractMemory<T> memory;

    protected double mutationRate;
    protected RealDistribution realDistribution;


    public SelfLearningHeuristic(Heuristic<T> heuristic, AbstractMemory<T> memory, double mutationRate, RealDistribution realDistribution) {
        this.heuristic = heuristic;
        this.memory = memory;
        this.realDistribution = realDistribution;
        this.mutationRate = mutationRate;
    }

    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean decision = heuristic.decide(slot, peek);
        if (memory.getStreetLength() < slot.getDistance()) {
            memory.setStreetLength (slot.getDistance());
        }
        if (decision) {
            memory.add(new Parameter<>(heuristic.getParam(), slot.getDistance()));
        }
        return decision;
    }

    @Override
    public T getParam() {
        return this.heuristic.getParam();
    }

    @Override
    public void setParam(T param) {
        this.heuristic.setParam(param);
    }

    @Override
    public SelfLearningHeuristic<T> copy() {
        Heuristic<T> heuristicCopy = this.heuristic.copy();
        if(realDistribution.sample() < mutationRate || memory.size() < memory.getMaxSize()){
            heuristicCopy.setParam(mutate());
        }else{
            heuristicCopy.setParam(memory.learnedParameter());
        }
        SelfLearningHeuristic<T> copy = makeCopy();
        copy.heuristic = heuristicCopy;
        copy.mutationRate = mutationRate;
        copy.memory = memory;
        copy.realDistribution = realDistribution;

        return copy;
    }

    protected abstract T mutate();
    protected abstract SelfLearningHeuristic<T> makeCopy();
}
