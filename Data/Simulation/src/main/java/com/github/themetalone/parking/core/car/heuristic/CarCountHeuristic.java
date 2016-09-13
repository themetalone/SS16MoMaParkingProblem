package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Implementation of the Car Count Heuristic. This heuristic utilizes the number of cars passed.
 * Created by steff on 18.08.2016.
 */
public class CarCountHeuristic implements Heuristic<Integer> {

    protected Integer threshold;
    protected Integer carCount = 0;
    protected int lastDistance = Integer.MAX_VALUE;

    /**
     * @param threshold minimal number of cars to be passed before choosing a parking spot
     */
    public CarCountHeuristic(Integer threshold) {
        this.threshold = threshold;
    }

    /**
     * @param slot the parking spot to be tested
     * @param peek the following parking spot
     * @return true iff a minimum of parked cars is passed and the current spot is empty and the next one isn't or of the car is mocing away from the destination
     */
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

    /**
     * @return number of cars passed
     */
    @Override
    public Integer getParam() {
        return threshold;
    }

    /**
     * @param param the new number of cars to pass before taking a parking spot
     */
    @Override
    public void setParam(Integer param) {
        this.threshold = param;
    }
}
