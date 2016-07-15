package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Implementation of the ''Decide after n candidates'' heuristic
 * Created by steff on 11.07.2016.
 */
public class DecideAfterNCandidatesHeuristic implements Heuristic {

    private final int n;
    private int passedCandidates;
    private int lastDistance = Integer.MAX_VALUE;

    /**
     * @param n the number of candidates to be passed by
     */
    public DecideAfterNCandidatesHeuristic(int n) {
        this.n = n;
        passedCandidates = 0;
    }

    /**
     * Decides if a parking spot should be taken
     * @param slot the parking spot to be tested
     * @param peek the following parking spot
     * @return true if and only if slot is empty AND ((peek is occupied AND n candidates are passed by) OR destination is already passed by)
     */
    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean turningPointPassed = slot.getDistance() > lastDistance;
        lastDistance = slot.getDistance();
        if (!slot.isOccupied()) {
            if (turningPointPassed) {
                return true;
            }
            passedCandidates++;
            if (passedCandidates > n) {
                return peek.isOccupied();
            }
        }
        return false;
    }

    @Override
    public Heuristic copy() {
        return new DecideAfterNCandidatesHeuristic(n);
    }

    @Override
    public String toString() {
        return "DecideAfterNCandidatesHeuristic[n:" + n + "]";
    }
}
