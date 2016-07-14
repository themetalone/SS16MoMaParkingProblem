package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 11.07.2016.
 */
public class ChooseBetterThanNHeuristic implements Heuristic{

    private final int threshold;
    private int lastDistance = Integer.MAX_VALUE;

    public ChooseBetterThanNHeuristic(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean turningPointPassed = slot.getDistance() > lastDistance;
        lastDistance = slot.getDistance();
        return !slot.isOccupied() && (((slot.getDistance() < threshold) && peek.isOccupied()) || turningPointPassed);
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
