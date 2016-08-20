package com.github.themetalone.parking.core.car.heuristic.selfLearning.memories;

import com.github.themetalone.parking.core.car.heuristic.selfLearning.Parameter;

import java.util.HashSet;
import java.util.stream.Stream;

/**
 * Created by steff on 18.08.2016.
 */
public abstract class AbstractMemory<T> extends HashSet<Parameter<T>> {

    protected final int maxSize;
    protected int streetLength = 1;

    public AbstractMemory(int initialCapacity) {
        super(initialCapacity);
        maxSize = initialCapacity;
    }

    @Override
    public final boolean add(Parameter<T> tParameter) {
        boolean parameterPresent = super.stream().filter((Parameter<T> p) -> p.getParam().equals(tParameter.getParam()))
                .findAny().isPresent();
        if (parameterPresent) {
            Parameter<T> presentParameter = super.stream().filter((Parameter<T> p) -> p.getParam().equals(tParameter.getParam()))
                    .findAny()
                    .get();
            if (presentParameter.getDistance() != tParameter.getDistance()) {
                super.remove(presentParameter);
                return super.add(tParameter);
            } else {
                return false;
            }
        }
        if (super.size() < maxSize) {
            return super.add(tParameter);
        }
        Parameter<T> worstParameter = super.stream().max((Parameter<T> p1, Parameter<T> p2) -> p1.getDistance() - p2.getDistance()).get();
        if (worstParameter.getDistance() > tParameter.getDistance()) {
            super.remove(worstParameter);
            return super.add(tParameter);
        }
        return false;
    }

    @Override
    public Stream<Parameter<T>> stream() {
        return super.stream();
    }

    @Override
    public Stream<Parameter<T>> parallelStream() {
        return super.parallelStream();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public abstract T learnedParameter();

    public int getStreetLength() {
        return streetLength;
    }

    public void setStreetLength(int streetLength) {
        this.streetLength = streetLength;
    }
}
