package com.github.themetalone.parking.core.data;

/**
 * Created by steff on 12.07.2016.
 */
public interface SimulationDataCollector {

    void putParkingSlot(int id, int distance);

    void putParkingData(int id, boolean occupied, long tick);

    void putCar(int id, String heuristic);

    void putCarData(int id, int distance, long tick);

    void close();
}
