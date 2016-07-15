package com.github.themetalone.parking.core;

import java.util.Observer;

/**
 * Base interface of the model objects of the simulation
 * Created by steff on 06.07.2016.
 */
public interface ModelObject extends Observer {
    /**
     *
     * @return id of the object
     */
    int getId();

    /**
     * Sets the id of the object
     * @param id to be set
     */
    void setId(int id);

}
