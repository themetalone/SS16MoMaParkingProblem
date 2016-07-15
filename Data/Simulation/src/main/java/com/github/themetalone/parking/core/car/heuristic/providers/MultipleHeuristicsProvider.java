package com.github.themetalone.parking.core.car.heuristic.providers;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import org.apache.commons.math3.distribution.IntegerDistribution;

import java.util.List;

/**
 * Provides a list of heuristics that are returned by random.
 * Created by steff on 15.07.2016.
 */
public class MultipleHeuristicsProvider implements HeuristicProvider {

    public List<Heuristic> getHeuristics() {
        return heuristics;
    }

    public void setHeuristics(List<Heuristic> heuristics) {
        this.heuristics = heuristics;
    }

    public IntegerDistribution getIntegerDistribution() {
        return integerDistribution;
    }

    public void setIntegerDistribution(IntegerDistribution integerDistribution) {
        this.integerDistribution = integerDistribution;
    }

    private List<Heuristic> heuristics;
    private IntegerDistribution integerDistribution;


    @Override
    public Heuristic getNewHeuristic() {
        return heuristics.get(sample());
    }

    private int sample(){
        int rand = integerDistribution.sample();
        while(rand < 0){
            rand += heuristics.size();
        }
        return rand % heuristics.size();
    }
}
