package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

/**
 * Implementation of the ''Space count'' heuristic. Utilizes the unoccupied spots after passing the first occupied spot
 * Created by steff on 11.07.2016.
 */
public class SpaceCountHeuristic implements Heuristic<Integer> {

    private int n;
    private int passedCandidates;
    private int lastDistance = Integer.MAX_VALUE;
    private boolean counting = false;

    /**
     * @param n the number of candidates to be passed by
     */
    public SpaceCountHeuristic(int n) {
        this.n = n;
        passedCandidates = 0;
    }

    /**
     * Decides if a parking spot should be taken
     *
     * @param slot the parking spot to be tested
     * @param peek the following parking spot
     * @return true if and only if slot is empty AND ((peek is occupied AND n candidates are passed by) OR destination is already passed by)
     */
    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean turningPointPassed = slot.getDistance() > lastDistance;
        lastDistance = slot.getDistance();
        if (!slot.isOccupied()) {
            if (turningPointPassed) {
                return true;
            }
            if (counting) {
                passedCandidates++;
                if (passedCandidates > n) {
                    return peek.isOccupied();
                }
            }
        }else{
            counting = true;
        }
        return false;
    }

    @Override
    public Heuristic<Integer> copy() {
        return new SpaceCountHeuristic(n);
    }

    /**
     * @return number of spots to pass after passing the first occupied one before choosing a spot
     */
    @Override
    public Integer getParam() {
        return n;
    }

    /**
     * @param param number of spots to pass after passing the first occupied one before choosing a spot
     */
    @Override
    public void setParam(Integer param) {
        this.n = param;
    }

    /**
     * @return 'SpaceCountHeuristic[n:${number of spots to pass}]
     */
    @Override
    public String toString() {
        return "SpaceCountHeuristic[n:" + n + "]";
    }
}
