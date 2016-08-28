package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Created by steff on 19.08.2016.
 */
public class BlockCountHeuristic implements Heuristic<Integer> {

    protected int blockLength;
    protected int passedCars = 0;
    protected boolean takeFreeSpot = false;
    protected int lastDistance = Integer.MAX_VALUE;

    public BlockCountHeuristic(int blockLength) {
        this.blockLength = blockLength;
    }

    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean turningPointPassed = slot.getDistance() > lastDistance;
        lastDistance = slot.getDistance();
        if (!slot.isOccupied()) {
            passedCars = 0;
            if (turningPointPassed) {
                return true;
            }
            if (takeFreeSpot) {
                return peek.isOccupied();
            }
        } else {
            passedCars++;
            if (passedCars >= blockLength) {
                takeFreeSpot = true;
            }
        }
        return false;
    }

    @Override
    public Heuristic<Integer> copy() {
        return new BlockCountHeuristic(blockLength);
    }

    @Override
    public Integer getParam() {
        return blockLength;
    }

    @Override
    public void setParam(Integer param) {
        this.blockLength = param;
    }
}
