package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Implementation of the Block Count Heuristic. This heuristic utilizes the number of parked cars without a vacant space in between.
 * Created by steff on 19.08.2016.
 */
public class BlockCountHeuristic implements Heuristic<Integer> {

    protected int blockLength;
    protected int passedCars = 0;
    protected boolean takeFreeSpot = false;
    protected int lastDistance = Integer.MAX_VALUE;

    /**
     * @param blockLength the threshold length of the car blocks
     */
    public BlockCountHeuristic(int blockLength) {
        this.blockLength = blockLength;
    }

    /**
     * @param slot the parking spot to be tested
     * @param peek the following parking spot
     * @return true iff a block of at least n neatly parked cars is passed and the next slot is occupied or the car is moving away from the destination
     */
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

    /**
     * @return the threshold block length
     */
    @Override
    public Integer getParam() {
        return blockLength;
    }

    /**
     * @param param the new threshold block length
     */
    @Override
    public void setParam(Integer param) {
        this.blockLength = param;
    }
}
