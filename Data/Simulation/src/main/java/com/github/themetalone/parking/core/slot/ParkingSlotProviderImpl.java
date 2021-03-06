package com.github.themetalone.parking.core.slot;

import com.github.themetalone.parking.core.data.SimulationDataCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by steff on 11.07.2016.
 */
public class ParkingSlotProviderImpl implements ParkingSlotProvider {

    private int id = -1;

    private Collection<ParkingSlot> parkingSlots;

    public SimulationDataCollector getSimulationDataCollector() {
        return simulationDataCollector;
    }

    public void setSimulationDataCollector(SimulationDataCollector simulationDataCollector) {
        this.simulationDataCollector = simulationDataCollector;
    }

    private SimulationDataCollector simulationDataCollector;

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
            this.id = id;
            ParkingSlot p = new ParkingSlotImpl(id, simulationDataCollector);
            parkingSlots.add(p);
            return p;
        }

    }

    @Override
    public ParkingSlot next() {
        id++;
        return new ParkingSlotImpl(id,simulationDataCollector);
    }
}
