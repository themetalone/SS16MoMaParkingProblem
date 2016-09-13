package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by steff on 20.08.2016.
 * Implements the Linear Operator heuristic. Uses a fading memory of the density of occupied spots
 */
public class LinearOperatorHeuristic implements Heuristic<List<Double>> {

    protected double velocity;
    protected double threshold;
    protected double average = 0;
    private int lastDistance = Integer.MAX_VALUE;
    private boolean takeNext = false;
    protected boolean firstCarEncountered = false;

    /**
     * @param velocity double, describes how fast the memory of passed spots fades
     * @param threshold double, >= 0, the density to find befor choosing a spot. Higher values refer higher densities
     */
    public LinearOperatorHeuristic(double velocity, double threshold) {
        this.velocity = velocity;
        this.threshold = threshold;
    }


    /**
     * @param slot the parking spot to be tested
     * @param peek the following parking spot
     * @return true iff the density is at least that of threshold and the current spot is empty and the following is occupied or the condition was already fulfilled at the last spot and the last spot was not occupied, this spot isn't and the next one is or the car is moving away from the destination
     */
    @SuppressWarnings("Duplicates")
    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        if (slot.isOccupied()) {
            firstCarEncountered = true;
        }
        boolean turningPointPassed = slot.getDistance() > lastDistance;
        lastDistance = slot.getDistance();
        if (firstCarEncountered) {
            average = average * velocity + (slot.isOccupied() ? 1 : 0);
        }
        boolean densityFulfilled = average > threshold;
        if (!slot.isOccupied()) {
            if (turningPointPassed || (firstCarEncountered && densityFulfilled && peek.isOccupied()) || (!peek.isOccupied() && takeNext)) {
                return true;
            }
            if (firstCarEncountered && densityFulfilled && !peek.isOccupied()) {
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

    /**
     * @return {@link List}&lt;double> = [velocity, threshold]
     */
    @Override
    public List<Double> getParam() {
        List<Double> result = new LinkedList<>();
        result.add(velocity);
        result.add(threshold);
        return result;
    }

    /**
     * @param param {@link List}&lt;double> = [velocity, threshold]
     */
    @Override
    public void setParam(List<Double> param) {
        if (param.size() < 2) {
            throw new IllegalArgumentException("Not enough parameters");
        }
        this.velocity = param.get(0);
        this.threshold = param.get(1);
    }
}
