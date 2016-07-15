package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Implementation of the ''Chose Better than n'' heuristic
 * Created by steff on 11.07.2016.
 */
public class ChooseBetterThanNHeuristic implements Heuristic{

    private final int threshold;
    private int lastDistance = Integer.MAX_VALUE;

    /**
     * @param threshold the distance a parking sport should be close or closer to the destination
     */
    public ChooseBetterThanNHeuristic(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Decides if a parking spot should be chosen
     * @param slot the parking spot to be tested
     * @param peek the following parking spot
     * @return true if and only if slot is empty AND (( peek is occupied AND slot is at most threshold far away from the destination) OR destination is already passed by)
     */
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
