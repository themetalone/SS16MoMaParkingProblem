package com.github.themetalone.parking.core.car;

import com.github.themetalone.parking.core.ModelObject;
import com.github.themetalone.parking.core.slot.ParkingSlot;

import java.util.Observer;

/**
 * Created by steff on 06.07.2016.
 */
public interface Car extends ModelObject {

    /**
     * Decides if the car chooses a parkingspot
     * @param parkingSlot the parking spot
     * @return true if the car chooses the parking sport. false otherwise
     */
    boolean decide(ParkingSlot parkingSlot);


}
