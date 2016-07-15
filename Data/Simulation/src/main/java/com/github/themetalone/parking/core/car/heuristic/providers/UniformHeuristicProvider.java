package com.github.themetalone.parking.core.car.heuristic.providers;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;

/**
 * Returns a heuristic
 * Created by steff on 11.07.2016.
 */
public class UniformHeuristicProvider implements HeuristicProvider {

    public Heuristic getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    private Heuristic heuristic;



    @Override
    public Heuristic getNewHeuristic() {
        return heuristic.copy();
    }
}
