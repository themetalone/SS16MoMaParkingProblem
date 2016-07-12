package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 11.07.2016.
 */
public class ChooseBetterThanNHeuristic implements Heuristic{

    private final int threshold;

    public ChooseBetterThanNHeuristic(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        return (slot.getDistance() < threshold) && !slot.isOccupied() && peek.isOccupied();
    }

    @Override
    public Heuristic copy() {
        return new ChooseBetterThanNHeuristic(threshold);
    }

    @Override
    public String toString(){
        return "ChooseBetterThanNHeuristic[threshold:"+threshold+"]";
    }
}
