package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 11.07.2016.
 */
public class DecideAfterNCandidatesHeuristic implements Heuristic {

    private final int n;
    private int passedCandidates;
    private int lastDistance = Integer.MAX_VALUE;

    public DecideAfterNCandidatesHeuristic(int n) {
        this.n = n;
        passedCandidates = 0;
    }

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
