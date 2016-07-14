package com.github.themetalone.parking.core.slot;

import com.github.themetalone.parking.core.car.Car;
import com.github.themetalone.parking.core.data.SimulationDataCollector;
import com.github.themetalone.parking.core.street.Street;

import java.util.Observable;

/**
 * Created by steff on 11.07.2016.
 */
public class ParkingSlotImpl implements ParkingSlot {

    private boolean persistent = false;

    private SimulationDataCollector simulationDataCollector;

    public ParkingSlotImpl(SimulationDataCollector simulationDataCollector) {
        this.simulationDataCollector = simulationDataCollector;
        this.id = getCounter();

    }

    public ParkingSlotImpl(int id, SimulationDataCollector simulationDataCollector) {
        this.simulationDataCollector = simulationDataCollector;
        this.id = id;
        counter = id;
    }

    public static int getCounter() {
        counter++;

        return counter;
    }

    private static int counter = -1;

    private boolean occupied;
    private int distance = 0;
    private int id;

    @Override
    public boolean isOccupied() {
        return occupied;
    }

    @Override
    public void occupy(Car car) {
        occupied = true;
    }

    @Override
    public void clear() {
        occupied = false;
    }

    @Override
    public int getDistance() {
        return distance;
    }

    @Override
    public void setDistance(int d) {
        this.distance = d;
        if (!persistent) {
            persistent = true;
            simulationDataCollector.putParkingSlot(id, distance);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((o instanceof Street) && (arg instanceof Long)) {
            simulationDataCollector.putParkingData(id, isOccupied(), (Long) arg);
        }
    }
}
