package com.github.themetalone.parking.core.simulations;

import com.github.themetalone.parking.core.data.SimulationDataCollector;
import com.github.themetalone.parking.core.street.Street;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by steff on 21.08.2016.
 */
public class MultipleCasesParallelSimulation implements Simulation, Observer {

    protected List<Street> streets;
    protected final long ticks;
    protected Collection<Thread> runningSimulations = new HashSet<>();
    private Logger LOG = LoggerFactory.getLogger(Simulation.class);
    private long totalWork = 0;
    private long remainingWork = 0;
    private int runningSimulationCounter = 0;
    private Collection<SimulationDataCollector> simulationDataCollectors = new HashSet<>();

    public MultipleCasesParallelSimulation(long ticks, List<Street> streets) {
        this.ticks = ticks;
        this.streets = streets;
    }

    @Override
    public void setSimulationDataCollector(SimulationDataCollector simulationDataCollector) {

    }

    public void setSimulationDataCollectors(Collection<SimulationDataCollector> simulationDataCollectors){
        this.simulationDataCollectors = simulationDataCollectors;
    }

    @Override
    public void simulate() {

        for (Street s : streets) {
            SimulationRun simRun = new SimulationRun(s, ticks);
            remainingWork += ticks;
            totalWork += ticks;
            Thread t = new Thread(simRun);
            runningSimulations.add(t);
            runningSimulationCounter++;
        }
        runningSimulations.forEach(Thread::start);
        LOG.info("Started {} simulations. This may take while. Have a coffee.", runningSimulationCounter);
        runningSimulations.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                LOG.error("Encountered {}:{}", e.getClass().getName(), e.getMessage());
            }
        });
        simulationDataCollectors.stream().forEach(SimulationDataCollector::close);
        LOG.info("Done");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof SimulationRun && arg instanceof Long) {
            long workDone = (long) arg;
            if (workDone == -1) {
                runningSimulationCounter--;
                LOG.info("A simulation finished. {} simulations remain", runningSimulationCounter);
            } else {
                remainingWork -= workDone;
            }
            LOG.info("{}% done", remainingWork / totalWork * 100);
        }
    }

    class SimulationRun extends Observable implements Runnable {

        Street street;
        long ticks;
        long workDone = 0;

        public SimulationRun(Street street, long ticks) {
            this.street = street;
            this.ticks = ticks;
        }

        @Override
        public void run() {
            while (ticks > 0) {
                ticks--;
                workDone++;
                street.tick();
                if (ticks % 1000 == 0) {
                    super.setChanged();
                    super.notifyObservers(workDone);
                    workDone = 0;
                }
                if (ticks == 0) {
                    super.setChanged();
                    super.notifyObservers((long) -1);
                }
            }
        }
    }

}
