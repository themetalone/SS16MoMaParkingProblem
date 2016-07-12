package com.github.themetalone.parking.hutchinson.street;

import com.github.themetalone.parking.core.car.CarProvider;
import com.github.themetalone.parking.core.slot.ParkingSlotProvider;
import com.github.themetalone.parking.core.street.Street;
import com.github.themetalone.parking.core.street.StreetProvider;
import org.apache.commons.math3.distribution.IntegerDistribution;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by steff on 06.07.2016.
 */
public class HutchinsonStreetProviderImpl implements StreetProvider {

    public void setCarProvider(CarProvider carProvider) {
        this.carProvider = carProvider;
    }

    public void setParkingSlotProvider(ParkingSlotProvider parkingSlotProvider) {
        this.parkingSlotProvider = parkingSlotProvider;
    }

    public IntegerDistribution getIntegerDistribution() {
        return integerDistribution;
    }

    public void setIntegerDistribution(IntegerDistribution integerDistribution) {
        this.integerDistribution = integerDistribution;
    }

    private IntegerDistribution integerDistribution;
    private CarProvider carProvider;
    private ParkingSlotProvider parkingSlotProvider;

    private List<Street> streets;
    private int streetlength;

    public HutchinsonStreetProviderImpl(int streetlength) {
        this.streetlength = streetlength;
        this.streets = new LinkedList<>();
    }

    public Collection<Street> getObjects() {
        return streets;
    }

    public Street getObject(int id) {
        Predicate<Street> findId = e -> e.getId() == id;
        if (this.streets.stream().anyMatch(findId)) {
            return this.streets.stream().filter(findId).findFirst().get();
        } else {
            Street newStreet = new HutchinsonStreetImpl(id, streetlength, carProvider, parkingSlotProvider, integerDistribution);
            this.streets.add(newStreet);
            return newStreet;
        }
    }

    @Override
    public Street next() {
        Street newStreet = new HutchinsonStreetImpl(streetlength, carProvider, parkingSlotProvider, integerDistribution);
        this.streets.add(newStreet);
        return newStreet;
    }
}
