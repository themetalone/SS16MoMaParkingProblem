package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.car.heuristic.selfLearning.Parameter;
import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 18.08.2016.
 */
public class CarCountHeuristic implements Heuristic<Integer> {

    protected Integer threshold;
    protected Integer carCount = 0;
    protected int lastDistance = Integer.MAX_VALUE;

    public CarCountHeuristic(Integer threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean turningPointPassed = slot.getDistance() > lastDistance;
        lastDistance = slot.getDistance();
        if (slot.isOccupied()) {
            carCount++;
        } else {
            if (turningPointPassed) {
                return true;
            }
            carCount++;
            if (carCount > threshold) {
                return peek.isOccupied();
            }
        }
        return false;
    }

    @Override
    public Heuristic<Integer> copy() {
        return new CarCountHeuristic(threshold);
    }

    @Override
    public Integer getParam() {
        return threshold;
    }

    @Override
    public void setParam(Integer param) {
        this.threshold = param;
    }
}
