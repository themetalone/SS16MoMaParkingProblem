package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Base Interface for heuristics
 * Created by steff on 11.07.2016.
 */
public interface Heuristic {

    /**
     * Decides if a parking slot is to be chosen by the car
     * @param slot the parking spot to be tested
     * @param peek the following parking spot
     * @return true if slot is free AND peek is occupied AND the heuristics favors the slot
     */
    boolean decide(ParkingSlot slot, ParkingSlot peek);

    /**
     * @return a new Heuristic object with the same configuration as this one
     */
    Heuristic copy();

}
