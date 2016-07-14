package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 11.07.2016.
 * Chooses the first free parking slot after N passed slots
 */
public class DecideAfterNSlotsHeuristic implements Heuristic {

    private final int n;
    private int stepsTaken;
    private int lastDistance = Integer.MAX_VALUE;

    public DecideAfterNSlotsHeuristic(int n) {
        this.n = n;
        stepsTaken = 0;
    }

    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean turningPointPassed = slot.getDistance() > lastDistance;
        lastDistance = slot.getDistance();
        if (turningPointPassed) {
            return !slot.isOccupied();
        }
        stepsTaken++;
        return stepsTaken >= n && !slot.isOccupied() && peek.isOccupied();
    }

    @Override
    public Heuristic copy() {
        return new DecideAfterNSlotsHeuristic(n);
    }

    @Override
    public String toString() {
        return "DecideAfterNSlotsHeuristic[n:" + n + "]";
    }
}
