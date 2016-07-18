package com.github.themetalone.parking.analysis.model;

/**
 * Created by steff on 15.07.2016.
 */
public class CarEntry {

    private int id;
    private int distance;
    private long tick;
    private String heuristic;

    public CarEntry(int id, int distance, long tick, String heuristic) {
        this.id = id;
        this.distance = distance;
        this.tick = tick;
        this.heuristic = heuristic;
    }

    public int getId() {
        return id;
    }

    public int getDistance() {
        return distance;
    }

    public long getTick() {
        return tick;
    }

    public String getHeuristic() {
        return heuristic;
    }
}

