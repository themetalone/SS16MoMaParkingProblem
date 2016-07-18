package com.github.themetalone.parking.core.car.heuristic.evolutionary;

import com.github.themetalone.parking.core.car.heuristic.DecideAfterNSlotsHeuristic;
import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.slot.ParkingSlot;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by steff on 18.07.2016.
 */
public class EvolutionaryDecideAfterNSlotsHeuristic extends DecideAfterNSlotsHeuristic {

    private static int evolutionaryN = -1;
    private static long totalWeight = 1;
    private static int streetLength = -1;
    private static int wait;
    private static int waited = 0;
    private static IntegerDistribution integerDistribution = null;
    private static Logger LOG = LoggerFactory.getLogger(EvolutionaryDecideAfterNSlotsHeuristic.class);

    private EvolutionaryDecideAfterNSlotsHeuristic(int n, int wait) {
        super(n);
        EvolutionaryDecideAfterNSlotsHeuristic.wait = wait;
    }

    public EvolutionaryDecideAfterNSlotsHeuristic(IntegerDistribution integerDistribution, int wait) {
        super(integerDistribution.sample());
        if (EvolutionaryDecideAfterNSlotsHeuristic.integerDistribution == null) {
            EvolutionaryDecideAfterNSlotsHeuristic.integerDistribution = integerDistribution;
        }
        EvolutionaryDecideAfterNSlotsHeuristic.wait = wait;
    }

    @Override
    public boolean decide(ParkingSlot slot, ParkingSlot peek) {
        if (slot.getDistance() > streetLength) {
            streetLength = slot.getDistance();
        }
        boolean decision = super.decide(slot, peek);
        if (decision) {
            waited++;
            if (waited > wait) {
                if (evolutionaryN == -1) {
                    evolutionaryN = n;
                }
                evolve(this.n, slot.getDistance());
            }
        }
        return decision;
    }

    @Override
    public Heuristic copy() {
        if (waited > wait) {
            return new EvolutionaryDecideAfterNSlotsHeuristic(evolutionaryN, wait);
        } else {
            return new EvolutionaryDecideAfterNSlotsHeuristic(integerDistribution, wait);
        }
    }

    private static void evolve(int n, int dist) {
        evolutionaryN = (int) ((totalWeight * (long) evolutionaryN + (long) getWeight(dist) * (long) n) / (totalWeight + getWeight(dist)));
        totalWeight += getWeight(dist);
    }

    private static int getWeight(int dist) {
        if (dist > streetLength / 4 * 3) {
            return 10;
        }
        if (dist > streetLength / 2) {
            return 5;
        }
        if (dist > streetLength / 4) {
            return 1;
        }
        return 0;

    }
}
