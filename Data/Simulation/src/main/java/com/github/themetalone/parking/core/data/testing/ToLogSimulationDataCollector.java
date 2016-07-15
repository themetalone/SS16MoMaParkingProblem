package com.github.themetalone.parking.core.data.testing;

import com.github.themetalone.parking.core.data.SimulationDataCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by steff on 14.07.2016.
 */
public class ToLogSimulationDataCollector implements SimulationDataCollector {

    private Logger LOG = LoggerFactory.getLogger(SimulationDataCollector.class);

    @Override
    public void putParkingSlot(int id, int distance) {
        LOG.info("ParkingSlot::id:{}, distance:{}", id, distance);
    }

    @Override
    public void putParkingData(int id, boolean occupied, long tick) {
        if (occupied) {
            LOG.info("ParkingSpot occupied::id:{}, tick:{}", id, tick);
        }
    }

    @Override
    public void putCar(int id, String heuristic) {
        LOG.info("Car::id:{}, heuristic:{}", id, heuristic);
    }

    @Override
    public void putCarData(int id, int distance, long tick) {
        if (distance > 0) {
            LOG.info("Car parked::id:{}, distance:{}, tick:{}", id, distance, tick);
        }

    }

    @Override
    public void close() {
        LOG.info("Closed");
    }
}
