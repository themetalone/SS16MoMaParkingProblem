package com.github.themetalone.parking.core.data;

/**
 * Created by steff on 12.07.2016.
 */
public interface SimulationDataCollector {

    void putParkingData(int id, int distance, boolean occupied, long tick);

    void putCarData(int id, int parkingSlotId, int distance, String heuristic, long tick);

}
