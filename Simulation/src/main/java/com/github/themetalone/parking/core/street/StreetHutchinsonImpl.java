package com.github.themetalone.parking.core.street;

import com.github.themetalone.parking.core.car.Car;
import com.github.themetalone.parking.core.car.CarProvider;
import com.github.themetalone.parking.core.slot.ParkingSlot;
import com.github.themetalone.parking.core.slot.ParkingSlotProvider;

import java.util.*;

/**
 * Created by steff on 06.07.2016.
 * An implementation of the street object following the model of Hutchinson et al (2012), Car Parking as Game Between Simple Heuristics
 */
public class StreetHutchinsonImpl extends Observable implements Street{


    private long streetLength;
    private CarProvider carProvider;
    private ParkingSlotProvider parkingSlotProvider;
    private long laneTurningPoint;
    private long laneEndPoint;

    public StreetHutchinsonImpl(long numberOfParkingSpots, CarProvider carProvider, ParkingSlotProvider parkingSlotProvider) {
        this.streetLength = numberOfParkingSpots;
        this.carProvider = carProvider;
        this.parkingSlotProvider = parkingSlotProvider;
        this.parkingSlotList = new LinkedList<>();
        for(long i=0;i <numberOfParkingSpots;i++){
            this.parkingSlotList.add(this.parkingSlotProvider.next());
        }
        this.laneParkingMap = new LinkedHashMap<>();
        this.laneCarMap = new LinkedHashMap<>();
        this.laneTurningPoint = streetLength;
        this.laneEndPoint = this.laneTurningPoint +streetLength-1;
    }

    public static long getCounter() {
        counter++;
        return counter;
    }

    public static long counter = -1;



    public void enterLane(Car car) {

    }

    public void tick() {

    }

    private List<ParkingSlot> parkingSlotList;
    private Map<Long, Long> laneParkingMap;
    private Map<Long, Long> laneCarMap;

    public long getId() {
        return 0;
    }

    public void setId(long id) {

    }

    public void update(Observable o, Object arg) {

    }
}
