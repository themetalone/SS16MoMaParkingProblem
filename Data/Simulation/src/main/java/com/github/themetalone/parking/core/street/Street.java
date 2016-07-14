package com.github.themetalone.parking.core.street;

import com.github.themetalone.parking.core.ModelObject;

/**
 * Created by steff on 06.07.2016.
 */
public interface Street extends ModelObject{

    /**
     * Execute one discrete time step
     */
    void tick();

}
