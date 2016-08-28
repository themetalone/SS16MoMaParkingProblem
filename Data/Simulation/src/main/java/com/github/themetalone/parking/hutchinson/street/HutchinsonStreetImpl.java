package com.github.themetalone.parking.hutchinson.street;

import com.github.themetalone.parking.core.car.Car;
import com.github.themetalone.parking.core.car.CarProvider;
import com.github.themetalone.parking.core.exceptions.NoNextCarException;
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

    private Logger LOG = LoggerFactory.getLogger(Street.class);

    private IntegerDistribution integerDistribution;
    private CarProvider carProvider;
    private ParkingSlotProvider parkingSlotProvider;
    private int laneTurningPoint;
    private int laneEndPoint;
    private int id;
    private long toa;
    private List<Integer> markedToLeaveLane;

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
        this.laneTurningPoint = numberOfParkingSpots;
        this.laneEndPoint = this.laneTurningPoint + numberOfParkingSpots;
        this.linkParkingSlotsToLane(laneParkingMap, parkingSlotList, numberOfParkingSpots - 1, this.laneEndPoint);
        toa = 0;
        LOG.info("Created HutchinsonStreet:: Parkingspots:{} Streetlength:{}", numberOfParkingSpots, numberOfParkingSpots);
    }

    public static int getCounter() {
        counter++;
        return counter;
    }

    public static int counter = -1;


    public void enterLane(Car car) {
        this.addObserver(carProvider.getObject(car.getId()));
        // set car into waiting position in the lane (so in the next tick it will be at 0
        laneCarMap.put(car.getId(), -1);
    }

    public void tick() {
        LOG.debug("tick");
        this.setChanged();
        this.notifyObservers(getTicker());
        markedToLeaveLane = new LinkedList<>();
        for (Integer carId : laneCarMap.keySet()) {
            // Next position for current car
            int newSpot = laneCarMap.get(carId) + 1;
            // Move the car one parking slot forward
            laneCarMap.put(carId, newSpot);
            LOG.debug("Moving Car {} to slot {}", carId, newSpot);
            if (newSpot == laneTurningPoint) {
                LOG.debug("Car {} is in the turning point", carId);
                continue;
            }
            if (newSpot > laneEndPoint) {
                LOG.debug("Car {} leaves the lane", carId);
                markedToLeaveLane.add(carId);
                this.deleteObserver(carProvider.getObject(carId));
                continue;
            }
            Integer peekId = laneParkingMap.get(newSpot + 1);
            ParkingSlot peek;
            if (peekId == null) {
                peek = parkingSlotProvider.getAlwaysOccupiedSlot();
            } else {
                peek = parkingSlotProvider.getObject(peekId);
            }
            if (carProvider.getObject(carId).decide(parkingSlotProvider.getObject(laneParkingMap.get(newSpot)), peek) != -1) {
                LOG.debug("Car {} parks in spot {} at tick {}", carId, newSpot, this.ticker);
                markedToLeaveLane.add(carId);
            }
        }
        if (toa == 0) {
            try {
                enterLane(carProvider.next());
                toa = getNextTOA();
                LOG.debug("New Car enters lane at tick {}. {} ticks until next car", this.ticker, toa);
            } catch (NoNextCarException e) {
               LOG.debug("No free car left. Just waiting");
            }
        } else {
            toa--;
        }
        leaveLane();
    }

    private long getNextTOA() {
        return (long) integerDistribution.sample();
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
        markedToLeaveLane.forEach(carId -> laneCarMap.remove(carId));
    }

    public void linkParkingSlotsToLane(Map<Integer, Integer> map, List<ParkingSlot> list, int singleLaneLength, int laneEndPoint) {

        for (int i = 0; i <= singleLaneLength; i++) {
            parkingSlotProvider.getObject(i).setDistance(singleLaneLength - i);
            map.put(i, list.get(i).getId());
            map.put(laneEndPoint - i, list.get(i).getId());
            ParkingSlot p = parkingSlotProvider.getObject(i);
            LOG.debug("Linking Parking Sport {} with distance {} to lane position {} and {}", p.getId(), p.getDistance(), i, laneEndPoint - i);
        }

    }

    public void setObservers(Collection<Observer> observers){
        observers.stream().forEach(this::addObserver);
    }

}
