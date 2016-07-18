package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 11.07.2016.
 * Chooses the first free parking slot after N passed slots
 */
public class DecideAfterNSlotsHeuristic implements Heuristic {

    protected final int n;
    private int stepsTaken;
    private int lastDistance = Integer.MAX_VALUE;

    /**
     *
     * @param n number of parking slots to be passed before picking a parking slot
     */
    public DecideAfterNSlotsHeuristic(int n) {
        this.n = n;
        stepsTaken = 0;
    }

    /**
     * Decides if a parking slot is to be chosen by a car
     * @param slot the parking spot to be tested
     * @param peek the following parking spot
     * @return return true if and only if slot is empty AND ((peek is occupied AND n slots or more are passed by) OR destination is already passed by)
     */
    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean turningPointPassed = slot.getDistance() > lastDistance;
        lastDistance = slot.getDistance();
        // If we start to get further away from the destination we will take the next possible spot
        if (turningPointPassed) {
            return !slot.isOccupied();
        }
        stepsTaken++;
        boolean thresholdPassed = stepsTaken >= n;
        boolean slotIsFree = !slot.isOccupied();
        boolean nextIsOccupied = peek.isOccupied();
        //              1               1                   1 => take the spot
        //              0               1                   1 => reject since to far away
        //              1               0                   1 => reject since not free
        //              1               0                   0 => reject since next spot is free
        return thresholdPassed && slotIsFree && nextIsOccupied;
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
