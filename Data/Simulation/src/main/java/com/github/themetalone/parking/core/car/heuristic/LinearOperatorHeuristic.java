package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by steff on 20.08.2016.
 */
public class LinearOperatorHeuristic implements Heuristic<List<Double>>{

    protected double velocity;
    protected double threshold;
    protected double average = 0;
    private int lastDistance = 0;
    private boolean takeNext = false;

    public LinearOperatorHeuristic(double velocity, double threshold) {
        this.velocity = velocity;
        this.threshold = threshold;
    }




    @SuppressWarnings("Duplicates")
    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean turningPointPassed = slot.getDistance() > lastDistance;
        lastDistance = slot.getDistance();
        average = average*velocity + (slot.isOccupied()?-1:0);
        boolean densityFulfilled = average > threshold;
        if (!slot.isOccupied()) {
            if (turningPointPassed || (densityFulfilled && peek.isOccupied()) || (!peek.isOccupied() && takeNext) ) {
                return true;
            }
            if(densityFulfilled && !peek.isOccupied()){
                takeNext = true;
            }
        } else {
            takeNext = false;
        }

        return false;
    }

    @Override
    public Heuristic<List<Double>> copy() {
        return new LinearOperatorHeuristic(velocity, threshold);
    }

    @Override
    public List<Double> getParam() {
        List<Double> result = new LinkedList<>();
        result.add(velocity);
        result.add(threshold);
        return result;
    }

    @Override
    public void setParam(List<Double> param) {
        if (param.size()<2){
            throw new IllegalArgumentException("Not enough parameters");
        }
        this.velocity = param.get(0);
        this.threshold = param.get(1);
    }
}
