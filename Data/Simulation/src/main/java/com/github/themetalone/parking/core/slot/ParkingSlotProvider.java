package com.github.themetalone.parking.core.slot;

import com.github.themetalone.parking.core.ModelProvider;
import com.github.themetalone.parking.core.car.Car;

import java.util.List;
import java.util.Observable;

/**
 * Created by steff on 06.07.2016.
 */
public interface ParkingSlotProvider extends ModelProvider<ParkingSlot> {

    List<ParkingSlot> getObjects();

    default ParkingSlot getAlwaysOccupiedSlot() {
        return new ParkingSlot() {
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
                return -1;
            }

            @Override
            public void setId(int id) {

            }

            @Override
            public void update(Observable o, Object arg) {

            }
        };
    }
}
