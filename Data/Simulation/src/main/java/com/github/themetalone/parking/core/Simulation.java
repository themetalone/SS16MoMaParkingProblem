package com.github.themetalone.parking.core;

import com.github.themetalone.parking.core.data.SimulationDataCollector;
import com.github.themetalone.parking.core.street.Street;
import com.github.themetalone.parking.core.street.StreetProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main Simulation class. Controls the simulation
 * Created by steff on 12.07.2016.
 */
public class Simulation {

    private Logger LOG = LoggerFactory.getLogger(Simulation.class);
    private long ticks;

    /**
     *
     * @param simulationDataCollector the SimulationDataCollector to be used
     */
    public void setSimulationDataCollector(SimulationDataCollector simulationDataCollector) {
        this.simulationDataCollector = simulationDataCollector;
    }

    private SimulationDataCollector simulationDataCollector;

    /**
     * Constructor
     * @param streetProvider the provider of a street object
     * @param ticks number of ticks the simulation will run. A tick is the discrete time unit of the simulation
     */
    public Simulation(StreetProvider streetProvider, long ticks) {
        this.ticks = ticks;
        this.streetProvider = streetProvider;
    }

    private StreetProvider streetProvider;

    /**
     * Simulates the Parking Problem
     */
    public void simulate() {
        LOG.info("Starting simulation with {} time steps", ticks);
        Street street = streetProvider.next();
        for (int i = 0; i < ticks; i++) {
            street.tick();
            if (i % 1000 == 0) {
                LOG.info("{} steps passed", i);
            }
        }
        LOG.info("Simulation done! :)");
        simulationDataCollector.close();
    }


}
