package com.github.themetalone.parking.core;

import java.util.Observer;

/**
 * Created by steff on 06.07.2016.
 */
public interface ModelObject extends Observer {

    long getId();
    void setId(long id);

}