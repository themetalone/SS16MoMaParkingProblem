package com.github.themetalone.parking.core;

import com.github.themetalone.parking.core.street.Street;
import com.github.themetalone.parking.core.street.StreetProvider;

/**
 * Created by steff on 12.07.2016.
 */
public class Simulation {


    public Simulation(StreetProvider streetProvider) {
        this.streetProvider = streetProvider;
    }

    private StreetProvider streetProvider;

    /**
     * Simulates the Parking Problem
     * @param length the number of ticks
     */
    public void simulate(int length){
        Street street = streetProvider.next();
        for(int i = 0;i<length;i++){
            street.tick();
        }

    }


}
