package com.github.themetalone.parking.core.car;

import com.github.themetalone.parking.core.ModelObject;
import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Base Interface for Cars
 * Created by steff on 06.07.2016.
 */
public interface Car extends ModelObject {

    /**
     * Decides if the car chooses a parkingspot
     * @param parkingSlot the parking spot
     * @param peek the next parkingspot.
     * @return n >= 0 if the car chooses the parking sport. -1 otherwise
     */
    int decide(ParkingSlot parkingSlot, ParkingSlot peek);


}
