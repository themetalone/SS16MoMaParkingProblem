package com.github.themetalone.parking.core;

import com.github.themetalone.parking.core.data.SimulationDataCollector;

/**
 * Created by steff on 20.08.2016.
 */
public interface Simulation {
    void setSimulationDataCollector(SimulationDataCollector simulationDataCollector);

    void simulate();
}
