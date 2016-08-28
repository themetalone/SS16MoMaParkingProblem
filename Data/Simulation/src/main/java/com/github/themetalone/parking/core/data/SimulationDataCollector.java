package com.github.themetalone.parking.core.data;

/**
 * base Interface for the data collection. Is responsible for the persistence of the simulation data
 * Created by steff on 12.07.2016.
 */
public interface SimulationDataCollector {

    /**
     * Puts a Parking Slot object to the database
     * @param id of the object
     * @param distance from the destination
     */
    void putParkingSlot(int id, int distance);

    /**
     * Puts simulation data of the parking slot to the database
     * @param id of the parking slot
     * @param occupied true if the parking slot is in use by a car
     * @param tick the discrete time step this data is putted
     */
    void putParkingData(int id, boolean occupied, long tick);

    /**
     * Puts a Car object to the database
     * @param id of the car
     * @param heuristic the car uses (usually you can use heuristic.toString())
     */
    void putCar(int id, String heuristic);

    /**
     * Puts simulation data of the car to the database
     * @param id of the car
     * @param distance the distance this car away from the destination
     * @param tick the discrete time step this data is putted
     */
    void putCarData(int id, int distance, long tick);

    /**
     * Finishes all open operations and closes the connection to the database
     */
    void close();

    void putHeuristicData(long tick, String heuristicName, String parameters, int distance);
}
