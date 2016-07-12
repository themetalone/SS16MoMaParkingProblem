package com.github.themetalone.parking.core.slot;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by steff on 11.07.2016.
 */
public class ParkingSlotProviderImpl implements ParkingSlotProvider {

    private Collection<ParkingSlot> parkingSlots;

    public ParkingSlotProviderImpl() {
        parkingSlots = new HashSet<>();
    }

    @Override
    public List<ParkingSlot> getObjects() {
        List<ParkingSlot> result = new LinkedList<>();
        result.addAll(parkingSlots);
        return result;
    }

    @Override
    public ParkingSlot getObject(int id) {
        Predicate<ParkingSlot> findId = p -> p.getId() == id;
        if (parkingSlots.stream().anyMatch(findId)) {
            return parkingSlots.stream().filter(findId).findFirst().get();
        } else {
            ParkingSlot p = new ParkingSlotImpl(id);
            parkingSlots.add(p);
            return p;
        }

    }

    @Override
    public ParkingSlot next() {
        return new ParkingSlotImpl();
    }
}
