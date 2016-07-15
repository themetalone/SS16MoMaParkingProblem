package com.github.themetalone.parking.core;

import com.github.themetalone.parking.core.exceptions.NoNextObjectException;

import java.util.Collection;

/**
 * Base Interface for the provision of model objects
 * Created by steff on 06.07.2016.
 */
public interface ModelProvider <T extends ModelObject> {

    /**
     *
     * @return a collection of known objects
     */
    Collection<T> getObjects();

    /**
     * Returns an object with the given ID
     * @param id of the object to have
     * @return an object with id
     */
    T getObject(int id);

    /**
     * Returns an object
     * @return an object
     * @throws NoNextObjectException if there is no other object to be returned
     */
    T next() throws NoNextObjectException;

}
