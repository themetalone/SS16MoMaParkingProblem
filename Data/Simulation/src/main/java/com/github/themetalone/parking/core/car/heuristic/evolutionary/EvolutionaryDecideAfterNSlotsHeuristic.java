package com.github.themetalone.parking.core.car.heuristic.evolutionary;

import com.github.themetalone.parking.core.car.heuristic.DecideAfterNSlotsHeuristic;
import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.data.SimulationDataCollector;
import com.github.themetalone.parking.core.slot.ParkingSlot;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * Created by steff on 18.07.2016.
 */
public class EvolutionaryDecideAfterNSlotsHeuristic extends DecideAfterNSlotsHeuristic {

    private static Logger LOG = LoggerFactory.getLogger(DecideAfterNSlotsHeuristic.class);
    private static int evolutionaryN = -1;
    private static int streetLength = -1;
    private static int wait;
    private static int waited = 0;
    private static IntegerDistribution integerDistribution = null;
    private static LinkedList<HeuristicParameterEntry> memory = new LinkedList<>();
    private static SimulationDataCollector simulationDataCollector;
    private static long count = -1;
    private static int bestQuarterWeight = 4;
    private static int bestHalfWeight = 1;
    private static int worstHalfWeight = 0;
    private static int worstQuarterWeight = 0;

    private static double mutationRate = 0.1;

    private EvolutionaryDecideAfterNSlotsHeuristic(int n, int wait) {
        super(n);
        EvolutionaryDecideAfterNSlotsHeuristic.wait = wait;
        if (simulationDataCollector != null) {
            simulationDataCollector.putHeuristicData(count, String.valueOf(super.n));
        }
    }

    public EvolutionaryDecideAfterNSlotsHeuristic(IntegerDistribution integerDistribution, int wait) {
        super(integerDistribution.sample());
        if (EvolutionaryDecideAfterNSlotsHeuristic.integerDistribution == null) {
            EvolutionaryDecideAfterNSlotsHeuristic.integerDistribution = integerDistribution;
        }
        if (simulationDataCollector != null) {
            simulationDataCollector.putHeuristicData(count, String.valueOf(super.n));
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
            addParameter(n, slot.getDistance());
        }
        return decision;
    }

    @Override
    public Heuristic copy() {
        count++;
        if ((waited > wait) && Math.random() > mutationRate) {
            return new EvolutionaryDecideAfterNSlotsHeuristic(evolutionaryN, wait);
        } else {
            return new EvolutionaryDecideAfterNSlotsHeuristic(integerDistribution, wait);
        }
    }

    private static void evolve() {
        LinkedList<HeuristicParameterEntry> sortedMemory = new LinkedList<>();
        sortedMemory.addAll(memory);
        sortedMemory.sort((o1, o2) -> o1.dist - o2.dist);
        long totalWeight = sortedMemory.subList(0, sortedMemory.size() / 3).stream().mapToLong(entry -> getWeight(entry.dist)).sum();
        if (totalWeight == 0) {
            return;
        }
        long weightedParam = sortedMemory.subList(0, sortedMemory.size() / 3).stream().mapToLong(entry -> getWeight(entry.dist) * entry.getParameter()).sum();
        evolutionaryN = (int) (weightedParam / totalWeight);
    }

    private static int getWeight(int dist) {
        if (dist > streetLength / 4 * 3) {
            return worstQuarterWeight;
        }
        if (dist > streetLength / 2) {
            return worstHalfWeight;
        }
        if (dist > streetLength / 4) {
            return bestHalfWeight;
        }
        return bestQuarterWeight;

    }

    private static void addParameter(int n, int dist) {
        HeuristicParameterEntry newEntry = new HeuristicParameterEntry(n, dist);
        if (memory.size() >= 30) {
            memory.pop();
        }
        memory.add(newEntry);
        evolve();
    }

    public void setSimulationDataCollector(SimulationDataCollector simulationDataCollector) {
        EvolutionaryDecideAfterNSlotsHeuristic.simulationDataCollector = simulationDataCollector;
    }

    public void setMutationRate(double mutationRate) {
        EvolutionaryDecideAfterNSlotsHeuristic.mutationRate = mutationRate;
    }

    public void setBestQuarterWeight(int bestQuarterWeight) {
        EvolutionaryDecideAfterNSlotsHeuristic.bestQuarterWeight = bestQuarterWeight;
    }

    public void setBestHalfWeight(int bestHalfWeight) {
        EvolutionaryDecideAfterNSlotsHeuristic.bestHalfWeight = bestHalfWeight;
    }

    public void setWorstHalfWeight(int worstHalfWeight) {
        EvolutionaryDecideAfterNSlotsHeuristic.worstHalfWeight = worstHalfWeight;
    }

    public void setWorstQuarterWeight(int worstQuarterWeight) {
        EvolutionaryDecideAfterNSlotsHeuristic.worstQuarterWeight = worstQuarterWeight;
    }

    @Override
    public String toString() {
        return String.valueOf(n);
    }

    private static class HeuristicParameterEntry {

        private final int parameter;
        private final int dist;

        public HeuristicParameterEntry(int parameter, int dist) {
            this.parameter = parameter;
            this.dist = dist;
        }

        public int getParameter() {
            return parameter;
        }

        public int getDist() {
            return dist;
        }
    }
}
