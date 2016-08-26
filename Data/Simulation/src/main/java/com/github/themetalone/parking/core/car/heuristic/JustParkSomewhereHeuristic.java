package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.car.heuristic.selfLearning.memories.IntegerMemory;
import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 20.07.2016.
 */
public class JustParkSomewhereHeuristic implements Heuristic<Object> {

    private int knownLength = 1;
    private int lastDistance = Integer.MAX_VALUE;


    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        // determine the street length
        if(knownLength == -1){
            knownLength = slot.getDistance();
        }
        // if the turning point is passed by
        if(lastDistance < slot.getDistance()){
            return !slot.isOccupied();
        }
        lastDistance = slot.getDistance();
        return !slot.isOccupied() && ((double)((knownLength - slot.getDistance()) / knownLength) >= Math.random()) && peek.isOccupied();
    }

    @Override
    public Heuristic<Object> copy() {
        return new JustParkSomewhereHeuristic();
    }

    @Override
    public Object getParam() {
        return new Object();
    }

    @Override
    public void setParam(Object param) {

    }
}
