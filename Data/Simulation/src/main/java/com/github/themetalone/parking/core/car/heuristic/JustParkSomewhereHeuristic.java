package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 20.07.2016.
 */
public class JustParkSomewhereHeuristic implements Heuristic {

    private int knownLength = 1;


    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        if (slot.getDistance() > knownLength) {
            knownLength = slot.getDistance();
        }

        return !slot.isOccupied() && ((knownLength - slot.getDistance()) / knownLength >= Math.random()) && peek.isOccupied();
    }

    @Override
    public Heuristic copy() {
        return new JustParkSomewhereHeuristic();
    }
}
