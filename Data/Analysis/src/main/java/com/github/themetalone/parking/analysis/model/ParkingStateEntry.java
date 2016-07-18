package com.github.themetalone.parking.analysis.model;

import java.math.BigInteger;

/**
 * Created by steff on 15.07.2016.
 */
public class ParkingStateEntry {

    private BigInteger state;
    private long tick;

    public ParkingStateEntry(BigInteger state, long tick) {

        this.state = state;
        this.tick = tick;
    }

    public BigInteger getState() {
        return state;
    }

    public long getTick() {
        return tick;
    }
}
