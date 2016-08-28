package com.github.themetalone.parking.core.car.heuristic.selfLearning.heuristic;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.Parameter;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.AbstractMemory;
import com.github.themetalone.parking.core.data.SimulationDataCollector;
import com.github.themetalone.parking.core.simulations.Simulation;
import com.github.themetalone.parking.core.slot.ParkingSlot;
import com.github.themetalone.parking.core.street.Street;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by steff on 18.08.2016.
 */
public abstract class SelfLearningHeuristic<T> implements Heuristic<T>, Observer {

    protected Heuristic<T> heuristic;
    protected AbstractMemory<T> memory;

    protected double mutationRate;
    protected RealDistribution realDistribution;

    protected SimulationDataCollector simulationDataCollector;
    private Long tick = (long) -1;
    protected Observable observable = null;

    protected boolean unused = false;

    public SelfLearningHeuristic(Heuristic<T> heuristic, AbstractMemory<T> memory, double mutationRate, RealDistribution realDistribution, SimulationDataCollector simulationDataCollector) {
        this.heuristic = heuristic;
        this.memory = memory;
        this.realDistribution = realDistribution;
        this.mutationRate = mutationRate;
        this.simulationDataCollector = simulationDataCollector;
    }

    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean decision = heuristic.decide(slot, peek);
        if (memory.getStreetLength() < slot.getDistance()) {
            memory.setStreetLength(slot.getDistance());
        }
        if (decision) {
            this.simulationDataCollector.putHeuristicData(tick, heuristic.getClass().getName(), getParamString(heuristic.getParam()), slot.getDistance());
            memory.add(new Parameter<>(heuristic.getParam(), slot.getDistance()));
            this.unused = true;
        }
        return decision;
    }

    private String getParamString(T param) {
        if (param instanceof Collection) {
            String result = "";
            boolean firstEntry = true;
            for (Object o : (Collection) param) {
                result += (firstEntry ? "" : ", ") + o.toString();
                firstEntry = false;
            }
            return result;
        } else {
            return param.toString();
        }
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
        if (realDistribution.sample() < mutationRate || memory.size() < memory.getMaxSize()) {
            heuristicCopy.setParam(mutate());
        } else {
            heuristicCopy.setParam(memory.learnedParameter());
        }
        SelfLearningHeuristic<T> copy = makeCopy();
        copy.heuristic = heuristicCopy;
        copy.mutationRate = mutationRate;
        copy.memory = memory;
        copy.realDistribution = realDistribution;
        observable.addObserver(copy);
        return copy;
    }

    protected abstract T mutate();

    protected abstract SelfLearningHeuristic<T> makeCopy();

    public abstract SelfLearningHeuristic<T> cleanCopy();

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Street && arg instanceof Long) {
            this.observable = (Observable) o;
            this.tick = (Long) arg;
            if (unused) {
                o.deleteObserver(this);
            }
        }
    }
}
