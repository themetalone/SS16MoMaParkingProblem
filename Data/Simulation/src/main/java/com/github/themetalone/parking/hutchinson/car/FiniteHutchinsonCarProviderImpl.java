package com.github.themetalone.parking.hutchinson.car;

import com.github.themetalone.parking.core.car.Car;
import com.github.themetalone.parking.core.car.CarProvider;
import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.car.heuristic.providers.HeuristicProvider;
import com.github.themetalone.parking.core.data.SimulationDataCollector;
import com.github.themetalone.parking.core.exceptions.NoNextCarException;
import com.github.themetalone.parking.core.slot.ParkingSlotProvider;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.*;
import java.util.function.Predicate;

/**
 * Created by steff on 09.07.2016.
 */
public class FiniteHutchinsonCarProviderImpl implements CarProvider, Observer {

    private Collection<Car> cars = new HashSet<>();
    private Queue<Car> freeCars = new LinkedList<>();

    public ParkingSlotProvider getParkingSlotProvider() {
        return parkingSlotProvider;
    }

    public void setParkingSlotProvider(ParkingSlotProvider parkingSlotProvider) {
        this.parkingSlotProvider = parkingSlotProvider;
    }

    private ParkingSlotProvider parkingSlotProvider;

    public SimulationDataCollector getSimulationDataCollector() {
        return simulationDataCollector;
    }

    public void setSimulationDataCollector(SimulationDataCollector simulationDataCollector) {
        this.simulationDataCollector = simulationDataCollector;
    }

    private SimulationDataCollector simulationDataCollector;

    public HeuristicProvider getHeuristicProvider() {
        return heuristicProvider;
    }

    public void setHeuristicProvider(HeuristicProvider heuristicProvider) {
        this.heuristicProvider = heuristicProvider;
    }

    private HeuristicProvider heuristicProvider;
    private int numberOfCars;
    private int pointer = -1;

    public int getLongestParkingTime() {
        return longestParkingTime;
    }

    public void setLongestParkingTime(int longestParkingTime) {
        this.longestParkingTime = longestParkingTime;
    }

    private int longestParkingTime;

    public RealDistribution getRealDistribution() {
        return realDistribution;
    }

    public void setRealDistribution(RealDistribution realDistribution) {
        this.realDistribution = realDistribution;
    }

    private RealDistribution realDistribution;

    public FiniteHutchinsonCarProviderImpl(int numberOfCars) {
        this.numberOfCars = numberOfCars;
    }

    @Override
    public Collection<Car> getObjects() {
        Collection<Car> newCollection = new HashSet<>();
        newCollection.addAll(cars);
        return newCollection;
    }

    @Override
    public Car getObject(int id) {
        Predicate<Car> findId = c -> c.getId() == id;
        if (cars.stream().anyMatch(findId)) {
            return cars.stream().filter(findId).findFirst().get();
        } else {

            if (id >= numberOfCars) {
                throw new IndexOutOfBoundsException();
            }

            Car newCar = new HutchinsonCarImpl(id, heuristicProvider.getNewHeuristic(), realDistribution, longestParkingTime, simulationDataCollector, parkingSlotProvider);
            cars.add(newCar);
            ((HutchinsonCarImpl) newCar).addObserver(this);
            return newCar;
        }

    }

    @Override
    public Car next() throws NoNextCarException {
        pointer++;
        if (pointer < numberOfCars) {
            Car newCar = new HutchinsonCarImpl(heuristicProvider.getNewHeuristic(), realDistribution, longestParkingTime, simulationDataCollector, parkingSlotProvider);
            cars.add(newCar);
            ((HutchinsonCarImpl) newCar).addObserver(this);
            return newCar;
        } else {
            try {
                return freeCars.remove();
            }catch (NoSuchElementException e){
                throw new NoNextCarException();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if ((o instanceof HutchinsonCarImpl) && (arg instanceof CarState)) {
            switch ((CarState) arg) {
                case InUse:
                    break;
                case Free:
                    Optional<Car> optCar = cars.stream().filter(c -> c.getId() == ((HutchinsonCarImpl) o).getId()).findFirst();
                    if (optCar.isPresent()) {
                        freeCars.add(optCar.get());
                    }
            }
        }
    }
}
