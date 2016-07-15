package com.github.themetalone.parking.core.car.heuristic.providers;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;

/**
 * Base Interface for the Heuristic Providers
 * Created by steff on 11.07.2016.
 */
public interface HeuristicProvider {

    /**
     * @return a new Heuristic
     */
    Heuristic getNewHeuristic();


}
