package com.github.themetalone.parking.core;

import java.util.Collection;

/**
 * Created by steff on 06.07.2016.
 */
public interface ModelProvider <T extends ModelObject> {

    Collection<T> getObjects();
    T getObject(int id);
    T next();

}
