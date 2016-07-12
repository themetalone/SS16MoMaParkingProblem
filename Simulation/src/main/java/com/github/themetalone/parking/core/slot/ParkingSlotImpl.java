package com.github.themetalone.parking.core.slot;

import com.github.themetalone.parking.core.car.Car;
import com.github.themetalone.parking.core.data.SimulationDataCollector;
import com.github.themetalone.parking.core.street.Street;

import java.util.Observable;

/**
 * Created by steff on 11.07.2016.
 */
public class ParkingSlotImpl implements ParkingSlot {

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

    private Car occupyingCar;
    private int distance = 0;
    private int id;

    @Override
    public boolean isOccupied() {
        return occupyingCar != null;
    }

    @Override
    public void occupy(Car car) {
        occupyingCar = car;
    }

    @Override
    public void clear() {
        occupyingCar = null;
    }

    @Override
    public int getDistance() {
        return distance;
    }

    @Override
    public void setDistance(int d) {
        this.distance = d;
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
        if((o instanceof Street)&& (arg instanceof Long)){
            simulationDataCollector.putParkingData(id,distance,isOccupied(), (Long)arg);
        }
    }
}