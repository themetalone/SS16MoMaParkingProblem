package com.github.themetalone.parking.hutchinson.street;

import com.github.themetalone.parking.core.car.Car;
import com.github.themetalone.parking.core.car.CarProvider;
import com.github.themetalone.parking.core.slot.ParkingSlot;
import com.github.themetalone.parking.core.slot.ParkingSlotProvider;
import com.github.themetalone.parking.core.street.Street;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by steff on 06.07.2016.
 * An implementation of the street object following the model of Hutchinson et al (2012), Car Parking as Game Between Simple Heuristics
 */
public class HutchinsonStreetImpl extends Observable implements Street {

    private Logger LOG = LoggerFactory.getLogger(HutchinsonStreetImpl.class);

    private IntegerDistribution integerDistribution;
    private int streetLength;
    private CarProvider carProvider;
    private ParkingSlotProvider parkingSlotProvider;
    private int laneTurningPoint;
    private int laneEndPoint;
    private int id;
    private long toa;
    private List<Integer> markedToLeaveLane;
    private LinkedList<Integer> carList = new LinkedList<>();

    public Long getTicker() {
        ticker++;
        return ticker;
    }

    private long ticker = -1;

    public HutchinsonStreetImpl(int numberOfParkingSpots, CarProvider carProvider, ParkingSlotProvider parkingSlotProvider, IntegerDistribution integerDistribution) {
        this.integerDistribution = integerDistribution;
        init(numberOfParkingSpots, carProvider, parkingSlotProvider);
    }

    public HutchinsonStreetImpl(int id, int numberOfParkingSpots, CarProvider carProvider, ParkingSlotProvider parkingSlotProvider, IntegerDistribution integerDistribution) {
        this.integerDistribution = integerDistribution;
        init(numberOfParkingSpots, carProvider, parkingSlotProvider);
        this.id = id;
        counter = id + 1;
    }

    private void init(int numberOfParkingSpots, CarProvider carProvider, ParkingSlotProvider parkingSlotProvider) {
        this.id = HutchinsonStreetImpl.getCounter();
        this.streetLength = numberOfParkingSpots;
        this.carProvider = carProvider;
        this.parkingSlotProvider = parkingSlotProvider;
        List<ParkingSlot> parkingSlotList = new LinkedList<>();
        for (long i = 0; i < numberOfParkingSpots; i++) {
            ParkingSlot pslot = this.parkingSlotProvider.next();
            this.addObserver(parkingSlotProvider.getObject(pslot.getId()));
            parkingSlotList.add(pslot);
        }
        this.laneParkingMap = new LinkedHashMap<>();
        this.laneCarMap = new LinkedHashMap<>();
        this.laneTurningPoint = streetLength;
        this.laneEndPoint = this.laneTurningPoint + streetLength - 1;
        this.linkParkingSlotsToLane(laneParkingMap, parkingSlotList, streetLength - 1, this.laneEndPoint);
        toa = 0;
        LOG.info("Created HutchinsonStreet:: Parkingspots:{} Streetlength:{}", numberOfParkingSpots, streetLength);
    }

    public static int getCounter() {
        counter++;
        return counter;
    }

    public static int counter = -1;


    public void enterLane(Car car) {
        this.addObserver(carProvider.getObject(car.getId()));
        carList.add(car.getId());
        laneCarMap.put(car.getId(), 0);
    }

    public void tick() {
        LOG.debug("tick");
        this.setChanged();
        this.notifyObservers(getTicker());
        markedToLeaveLane = new LinkedList<>();
        for (Integer carId : carList) {
            // Next position for current car
            int newSpot = laneCarMap.get(carId) + 1;
            // Move the car one parking slot forward
            laneCarMap.put(carId, newSpot);
            Integer peekId = laneParkingMap.get(newSpot + 1);
            ParkingSlot peek;
            LOG.debug("Moving Car {} to slot {}", carId, newSpot);
            if (peekId == null) {
                peek = new AlwaysOccupiedParkingSlot();
            } else {
                peek = parkingSlotProvider.getObject(peekId);
            }
            if (newSpot > laneEndPoint) {
                LOG.debug("Car {} leaves the lane", carId);
                markedToLeaveLane.add(carId);
                this.deleteObserver(carProvider.getObject(carId));
                continue;
            }
            if (carProvider.getObject(carId).decide(parkingSlotProvider.getObject(laneParkingMap.get(newSpot)), peek) != -1) {
                LOG.info("Car {} parks in spot {} at tick {}", carId, newSpot, this.ticker);
                markedToLeaveLane.add(carId);
            }
        }
        if (toa == 0) {
            enterLane(carProvider.next());
            toa = getNextTOA();
            LOG.info("New Car enters lane at tick {}. {} ticks until next car", this.ticker, toa);
        } else {
            toa--;
        }
        leaveLane();
    }

    private long getNextTOA() {
        long ticks = (long) integerDistribution.sample();
        return ticks;
    }

    private Map<Integer, Integer> laneParkingMap;
    private Map<Integer, Integer> laneCarMap;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void update(Observable o, Object arg) {

    }

    private void leaveLane() {
        markedToLeaveLane.forEach(carId -> {
            laneCarMap.remove(carId);
            carList.remove(carId);
        });
    }

    public void linkParkingSlotsToLane(Map<Integer, Integer> map, List<ParkingSlot> list, int singleLaneLength, int laneEndPoint) {

        for (int i = 0; i <= singleLaneLength; i++) {
            parkingSlotProvider.getObject(i).setDistance(singleLaneLength - i);
            map.put(i, list.get(i).getId());
            map.put(laneEndPoint - i, list.get(i).getId());
        }

    }

    public class AlwaysOccupiedParkingSlot implements ParkingSlot {

        @Override
        public boolean isOccupied() {
            return true;
        }

        @Override
        public void occupy(Car car) {

        }

        @Override
        public void clear() {

        }

        @Override
        public int getDistance() {
            return 0;
        }

        @Override
        public void setDistance(int d) {

        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public void setId(int id) {

        }

        @Override
        public void update(Observable o, Object arg) {

        }
    }
}