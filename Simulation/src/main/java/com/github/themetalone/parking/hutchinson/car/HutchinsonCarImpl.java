package com.github.themetalone.parking.hutchinson.car;

import com.github.themetalone.parking.core.car.Car;
import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.car.heuristic.HeuristicProvider;
import com.github.themetalone.parking.core.slot.ParkingSlot;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import java.util.Observable;

/**
 * Created by steff on 09.07.2016.
 */
public class HutchinsonCarImpl implements Car {

    public static int getCounter() {
        counter++;
        return counter;
    }

    private RealDistribution realDistribution;
    public static int counter = -1;
    private int id;
    private Heuristic heuristic;
    private int parkingTime = -1;
    private ParkingSlot parkingSlot = null;
    private int longestParkingTime;

    public HutchinsonCarImpl(Heuristic heuristic, RealDistribution realDistribution, int longestParkingTime) {
        this.longestParkingTime = longestParkingTime;
        this.realDistribution = realDistribution;
        this.id = getCounter();
        this.heuristic = heuristic;
    }

    public HutchinsonCarImpl(int id, Heuristic heuristic, RealDistribution realDistribution, int longestParkingTime) {
        this.longestParkingTime = longestParkingTime;
        this.realDistribution = realDistribution;
        this.heuristic = heuristic;
        this.id = id;
    }

    @Override
    public int decide(ParkingSlot parkingSlot, ParkingSlot peek) {
        boolean decision = heuristic.decide(parkingSlot, peek);
        if (decision) {
            parkingTime = getOccupationTime();
            this.parkingSlot = parkingSlot;
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
            parkingSlot.clear();
            parkingSlot = null;
        }
    }

    private int getOccupationTime() {
        double sample = realDistribution.sample();
        if(sample > longestParkingTime){
            return longestParkingTime;
        }
        if(sample < 0){
            return 0;
        }
        return (int)sample;
    }
}
