package com.github.themetalone.parking.hutchinson.car;

import com.github.themetalone.parking.core.car.Car;
import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.data.SimulationDataCollector;
import com.github.themetalone.parking.core.slot.ParkingSlot;
import com.github.themetalone.parking.core.slot.ParkingSlotProvider;
import com.github.themetalone.parking.core.street.Street;
import org.apache.commons.math3.distribution.RealDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;

/**
 * Created by steff on 09.07.2016.
 */
public class HutchinsonCarImpl extends Observable implements Car {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HutchinsonCarImpl that = (HutchinsonCarImpl) o;

        return id == that.id;

    }

    private ParkingSlotProvider parkingSlotProvider;

    public static int getCounter() {
        counter++;
        return counter;
    }

    private Logger LOG = LoggerFactory.getLogger(HutchinsonCarImpl.class);
    private RealDistribution realDistribution;
    public static int counter = -1;
    private int id;
    private Heuristic heuristic;
    private int parkingTime = -1;
    private Integer parkingSlot = null;
    private int longestParkingTime;
    private SimulationDataCollector simulationDataCollector;
    private boolean putData = false;

    public HutchinsonCarImpl(Heuristic heuristic, RealDistribution realDistribution, int longestParkingTime, SimulationDataCollector simulationDataCollector, ParkingSlotProvider parkingSlotProvider) {
        this.simulationDataCollector = simulationDataCollector;
        this.longestParkingTime = longestParkingTime;
        this.realDistribution = realDistribution;
        this.id = getCounter();
        this.heuristic = heuristic;
        this.parkingSlotProvider = parkingSlotProvider;
        this.simulationDataCollector.putCar(this.id, this.heuristic.toString());
    }

    public HutchinsonCarImpl(int id, Heuristic heuristic, RealDistribution realDistribution, int longestParkingTime, SimulationDataCollector simulationDataCollector, ParkingSlotProvider parkingSlotProvider) {
        this.simulationDataCollector = simulationDataCollector;
        this.longestParkingTime = longestParkingTime;
        this.realDistribution = realDistribution;
        this.heuristic = heuristic;
        this.id = id;
        this.parkingSlotProvider = parkingSlotProvider;
        this.simulationDataCollector.putCar(this.id, this.heuristic.toString());
    }

    @Override
    public int decide(ParkingSlot parkingSlot, ParkingSlot peek) {
        boolean decision = heuristic.decide(parkingSlot, peek);
        if (decision) {
            LOG.debug("Car {} chooses {} with distance {}", id, parkingSlot.getId(), parkingSlot.getDistance());
            putData = true;
            this.parkingSlot = parkingSlot.getId();
            parkingSlotProvider.getObject(this.parkingSlot).occupy(this);
            parkingTime = getOccupationTime();
            return parkingTime;
        }
        return -1;
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
        parkingTime--;
        if (parkingTime == 0 && parkingSlot != null) {
            LOG.debug("Car {} leaves {}", id, parkingSlot);
            parkingSlotProvider.getObject(parkingSlot).clear();
            parkingSlot = null;
            o.deleteObserver(this);
            putData = false;
            parkingTime = -1;
            heuristic = heuristic.copy();
            setChanged();
            notifyObservers(CarState.Free);
        }
        if ((o instanceof Street) && (arg instanceof Long)) {
            if (putData) {
                Integer distance = parkingSlot == null ? -1 : parkingSlotProvider.getObject(parkingSlot).getDistance();
                // Write the data set to the database. -1 since the decision is made in the previous click
                simulationDataCollector.putCarData(id, distance, (Long) arg - 1);
                putData = false;
            }
        }

    }

    @Override
    public int hashCode() {
        return this.id;
    }


    private int getOccupationTime() {
        double sample = realDistribution.sample();
        if (sample > longestParkingTime) {
            sample = longestParkingTime;
        }
        if (sample < 0) {
            sample = 0;
        }
        return (int) sample + parkingSlotProvider.getObject(parkingSlot).getDistance() * 5;
    }
}
