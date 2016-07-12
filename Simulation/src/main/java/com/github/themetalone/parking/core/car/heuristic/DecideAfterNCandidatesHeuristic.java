package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 11.07.2016.
 */
public class DecideAfterNCandidatesHeuristic implements Heuristic{

    private final int n;
    private int passedCandidates;

    public DecideAfterNCandidatesHeuristic(int n) {
        this.n = n;
        passedCandidates = 0;
    }

    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        if(!slot.isOccupied()){
            passedCandidates++;
            if(passedCandidates>n){
                return peek.isOccupied();
            }
        }
        return false;
    }

    @Override
    public Heuristic copy() {
        return new DecideAfterNCandidatesHeuristic(n);
    }
}
