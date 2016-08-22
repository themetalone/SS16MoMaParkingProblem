package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 20.07.2016.
 */
public class JustParkSomewhereHeuristic implements Heuristic<Object> {

    private int knownLength = 1;
    private int lastDistance = -1;


    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        if (lastDistance == -1) {
            lastDistance = slot.getDistance();
        }
        if (lastDistance >= slot.getDistance()) {
            lastDistance = slot.getDistance();
        }
        if (lastDistance < slot.getDistance()) {
            return true;
        }
        if (slot.getDistance() > knownLength) {
            knownLength = slot.getDistance();
        }

        return !slot.isOccupied() && ((knownLength - slot.getDistance()) / knownLength >= Math.random()) && peek.isOccupied();
    }

    @Override
    public Heuristic<Object> copy() {
        return new JustParkSomewhereHeuristic();
    }

    @Override
    public Object getParam() {
        return new Object();
    }

    @Override
    public void setParam(Object param) {

    }
}
