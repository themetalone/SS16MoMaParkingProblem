package com.github.themetalone.parking.hutchinson.car;

import com.github.themetalone.parking.core.car.Car;
import com.github.themetalone.parking.core.car.CarProvider;
import com.github.themetalone.parking.core.car.heuristic.HeuristicProvider;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by steff on 09.07.2016.
 */
public class FiniteHutchinsonCarProviderImpl implements CarProvider {

    private Collection<Car> cars = new HashSet<>();

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
        Stream<Car> stream = cars.parallelStream();
        if (stream.anyMatch(findId)) {
            return stream.filter(findId).findFirst().get();
        } else {

            if (id >= numberOfCars) {
                throw new IndexOutOfBoundsException();
            }

            Car newCar = new HutchinsonCarImpl(id, heuristicProvider.getNewHeuristic(), realDistribution, longestParkingTime);
            cars.add(newCar);
            return newCar;
        }

    }

    @Override
    public Car next() {
        pointer++;
        if (pointer < numberOfCars) {
            Car newCar = new HutchinsonCarImpl(heuristicProvider.getNewHeuristic(), realDistribution, longestParkingTime);
            cars.add(newCar);
            return newCar;
        } else {
            pointer = 0;
            return getObject(pointer);
        }
    }
}
