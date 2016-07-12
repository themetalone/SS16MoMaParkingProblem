package com.github.themetalone.parking.core.data;

/**
 * Created by steff on 12.07.2016.
 */
public interface SimulationDataCollector {

    void putParkingData(int id, boolean occupied, int tick);

    void putCarData(int id, int parkingSlotId, int distance, int heuristic, int tick);

}
