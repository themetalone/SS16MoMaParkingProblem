package com.github.themetalone.parking.core.street;

import com.github.themetalone.parking.core.car.CarProvider;
import com.github.themetalone.parking.core.slot.ParkingSlotProvider;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by steff on 06.07.2016.
 */
public class StreetHutchinsonImplProvider implements StreetProvider {

    public void setCarProvider(CarProvider carProvider) {
        this.carProvider = carProvider;
    }

    public void setParkingSlotProvider(ParkingSlotProvider parkingSlotProvider) {
        this.parkingSlotProvider = parkingSlotProvider;
    }

    private CarProvider carProvider;
    private ParkingSlotProvider parkingSlotProvider;

    private List<Street> streets;
    private long streetlength;

    public StreetHutchinsonImplProvider(long streetlength) {
        this.streetlength = streetlength;
        this.streets = new LinkedList<>();
    }

    public Collection<Street> getObjects() {
        return streets;
    }

    public Street getObject(long id) {
        Predicate<Street> findId = e -> e.getId() == id;
        if (this.streets.stream().anyMatch(findId)) {
            return this.streets.stream().filter(findId).findFirst().get();
        } else {
            return this.next();
        }
    }

    @Override
    public Street next() {
        Street newStreet = new StreetHutchinsonImpl(streetlength, carProvider,parkingSlotProvider);
        this.streets.add(newStreet);
        return newStreet;
    }
}
