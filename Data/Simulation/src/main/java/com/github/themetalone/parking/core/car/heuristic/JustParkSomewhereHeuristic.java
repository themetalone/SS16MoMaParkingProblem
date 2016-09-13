package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 20.07.2016.
 * Dummy heuristic. Chooses a parking spot randomly.
 */
public class JustParkSomewhereHeuristic implements Heuristic<Object> {

    private int knownLength = 1;
    private int lastDistance = Integer.MAX_VALUE;


    /**
     * @param slot the parking spot to be tested
     * @param peek the following parking spot
     * @return true with a probability of (s-d)/s for s the street length and d the current distance to the destination or if the car moves away from the destination
     */
    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        // determine the street length
        if(knownLength == -1){
            knownLength = slot.getDistance();
        }
        // if the turning point is passed by
        if(lastDistance < slot.getDistance()){
            return !slot.isOccupied();
        }
        lastDistance = slot.getDistance();
        return !slot.isOccupied() && ((double)((knownLength - slot.getDistance()) / knownLength) >= Math.random()) && peek.isOccupied();
    }

    @Override
    public Heuristic<Object> copy() {
        return new JustParkSomewhereHeuristic();
    }

    /**
     * @return new {@link Object}
     */
    @Override
    public Object getParam() {
        return new Object();
    }

    /**
     * Does nothing
     * @param param anything
     */
    @Override
    public void setParam(Object param) {

    }
}
