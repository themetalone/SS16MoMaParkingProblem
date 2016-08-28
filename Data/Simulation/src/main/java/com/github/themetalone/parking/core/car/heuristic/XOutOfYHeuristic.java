package com.github.themetalone.parking.core.car.heuristic;

import com.github.themetalone.parking.core.slot.ParkingSlot;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by steff on 19.08.2016.
 */
public class XOutOfYHeuristic implements Heuristic<List<Integer>> {

    protected int x;
    protected int y;
    private int lastDistance = Integer.MAX_VALUE;
    protected LinkedList<Integer> yBlock = new LinkedList<>();
    protected boolean takeNext= false;

    public XOutOfYHeuristic(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        boolean turningPointPassed = slot.getDistance() > lastDistance;
        lastDistance = slot.getDistance();
        boolean densityFulfilled = this.checkCondition(slot.isOccupied());
        if (!slot.isOccupied()) {
            if (turningPointPassed || (densityFulfilled && peek.isOccupied()) || (!peek.isOccupied() && takeNext) ) {
                return true;
            }
            if(densityFulfilled && !peek.isOccupied()){
                takeNext = true;
            }
        } else {
            takeNext = false;
        }

        return false;
    }

    protected boolean checkCondition(boolean slot) {
        yBlock.add(slot ? 1 : 0);
        if (yBlock.size() > y) {
            yBlock.pop();
        }
        return (yBlock.size() == y) && (yBlock.stream().mapToInt(p -> p).sum() >= x);
    }

    @Override
    public Heuristic<List<Integer>> copy() {
        return new XOutOfYHeuristic(x, y);
    }

    @Override
    public List<Integer> getParam() {
        List<Integer> result = new LinkedList<>();
        result.add(x);
        result.add(y);
        return result;
    }

    @Override
    public void setParam(List<Integer> param) {
        if (param.size() < 2) {
            throw new IllegalArgumentException("param list needs to contain exactly 2 parameters");
        }
        this.x = param.get(0);
        this.y = param.get(1);
    }
}
