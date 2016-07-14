package com.github.themetalone.parking.core.slot;

import com.github.themetalone.parking.core.ModelObject;
import com.github.themetalone.parking.core.car.Car;

/**
 * Created by steff on 06.07.2016.
 */
public interface ParkingSlot extends ModelObject{

    /**
     * @return true iff a car is in the spot. false otherwise
     */
    boolean isOccupied();

    /**
     * Occupies the parking slot with a car
     * @param car the car parking in this slot
     */
    void occupy(Car car);

    /**
     * removes any parking car from the slot
     */
    void clear();

    int getDistance();
    void setDistance(int d);

}
