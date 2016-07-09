package com.github.themetalone.parking.core.slot;

import com.github.themetalone.parking.core.ModelProvider;

import java.util.Collection;
import java.util.List;

/**
 * Created by steff on 06.07.2016.
 */
public interface ParkingSlotProvider extends ModelProvider<ParkingSlot> {

    List<ParkingSlot> getObjects();
}
