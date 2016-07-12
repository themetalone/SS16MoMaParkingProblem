package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 11.07.2016.
 */
public interface Heuristic {

    boolean decide(ParkingSlot slot, ParkingSlot peek);

    Heuristic copy();

}
