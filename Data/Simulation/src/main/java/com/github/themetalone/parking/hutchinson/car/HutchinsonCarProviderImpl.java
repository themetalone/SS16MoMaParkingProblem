package com.github.themetalone.parking.hutchinson.car;

import com.github.themetalone.parking.core.car.Car;
import com.github.themetalone.parking.core.car.CarProvider;
import com.github.themetalone.parking.core.car.heuristic.providers.HeuristicProvider;
import com.github.themetalone.parking.core.data.SimulationDataCollector;
import com.github.themetalone.parking.core.exceptions.NoNextCarException;
import com.github.themetalone.parking.core.slot.ParkingSlotProvider;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.Collection;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeSet;

/**
 * Created by steff on 19.07.2016.
 */
public class HutchinsonCarProviderImpl implements CarProvider, Observer {

    private int counter = -1;
    private Collection<Car> cars = new TreeSet<>((c1, c2) -> c1.getId() - c2.getId());
    private ParkingSlotProvider parkingSlotProvider;
    private SimulationDataCollector simulationDataCollector;
    private HeuristicProvider heuristicProvider;
    private int longestParkingTime;
    private RealDistribution realDistribution;


    @Override
    public Collection<Car> getObjects() {
        return cars;
    }

    @Override
    public Car getObject(int id) {
        if (cars.stream().filter(c -> c.getId() == id).findFirst().get() != null) {
            return cars.stream().filter(c -> c.getId() == id).findFirst().get();
        } else {
            Car car = new HutchinsonCarImpl(id, heuristicProvider.getNewHeuristic(), realDistribution, longestParkingTime, simulationDataCollector, parkingSlotProvider);
            cars.add(car);
            return car;
        }
    }

    @Override
    public Car next() {
        Car car = new HutchinsonCarImpl(heuristicProvider.getNewHeuristic(), realDistribution, longestParkingTime, simulationDataCollector, parkingSlotProvider);
        cars.add(car);
        return car;
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((o instanceof HutchinsonCarImpl) && (arg instanceof CarState)) {
            switch ((CarState) arg) {
                case InUse:
                    break;
                case Free:
                    Car observable = (HutchinsonCarImpl) o;
                    cars.remove(observable);
            }
        }
    }

    public void setCars(Collection<Car> cars) {
        this.cars = cars;
    }

    public void setParkingSlotProvider(ParkingSlotProvider parkingSlotProvider) {
        this.parkingSlotProvider = parkingSlotProvider;
    }

    public void setSimulationDataCollector(SimulationDataCollector simulationDataCollector) {
        this.simulationDataCollector = simulationDataCollector;
    }

    public void setHeuristicProvider(HeuristicProvider heuristicProvider) {
        this.heuristicProvider = heuristicProvider;
    }

    public void setLongestParkingTime(int longestParkingTime) {
        this.longestParkingTime = longestParkingTime;
    }

    public void setRealDistribution(RealDistribution realDistribution) {
        this.realDistribution = realDistribution;
    }
}
