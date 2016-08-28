package com.github.themetalone.parking.core.car.heuristic.selfLearning;

/**
 * Created by steff on 18.08.2016.
 */
public class Parameter<T> {

    private T param;
    private int distance;

    public Parameter(T param, int distance) {
        this.param = param;
        this.distance = distance;
    }

    public T getParam() {
        return param;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Parameter<?> parameter = (Parameter<?>) o;

        if (distance != parameter.distance) return false;
        return param != null ? param.equals(parameter.param) : parameter.param == null;

    }

    @Override
    public int hashCode() {
        int result = param != null ? param.hashCode() : 0;
        result = 31 * result + distance;
        return result;
    }
}
