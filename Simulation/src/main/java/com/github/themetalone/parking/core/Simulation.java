package com.github.themetalone.parking.core;

import com.github.themetalone.parking.core.street.Street;
import com.github.themetalone.parking.core.street.StreetProvider;

/**
 * Created by steff on 12.07.2016.
 */
public class Simulation {


    private long ticks;

    public Simulation(StreetProvider streetProvider, long ticks) {
        this.ticks = ticks;
        this.streetProvider = streetProvider;
    }

    private StreetProvider streetProvider;

    /**
     * Simulates the Parking Problem
     */
    public void simulate(){
        Street street = streetProvider.next();
        for(int i = 0;i<ticks;i++){
            street.tick();
        }

    }


}
