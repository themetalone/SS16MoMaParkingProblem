package com.github.themetalone.parking.core.car.heuristic.providers.selfLearning;

import com.github.themetalone.parking.core.car.heuristic.Heuristic;
import com.github.themetalone.parking.core.car.heuristic.providers.HeuristicProvider;
import com.github.themetalone.parking.core.car.heuristic.selfLearning.heuristic.SelfLearningHeuristic;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by steff on 28.08.2016.
 */
public class MultipleSelfLearningMutantHeuristicsProvider implements HeuristicProvider, Observer {

    private final List<SelfLearningHeuristic> mutants;
    private final Heuristic population;
    private final double mutationRate;
    private final RealDistribution realDistribution;
    private final int provideAtMostOfEach;

    private final ArrayList<Integer> providedTillNow;
    private IntegerDistribution mutantDistribution;

    private Observable observable = null;

    /**
     *
     * @param mutants list of mutant heuristics
     * @param population heuristic of the population
     * @param mutationRate percentage of mutant heuristics in the whole population
     * @param realDistribution real distribution in the range 0 to 1
     * @param provideAtMostOfEach upper limit of occurences for each mutant heuristic
     */
    public MultipleSelfLearningMutantHeuristicsProvider(List<SelfLearningHeuristic> mutants, Heuristic population, double mutationRate, RealDistribution realDistribution, int provideAtMostOfEach) {
        this.mutants = mutants;
        this.population = population;
        this.mutationRate = mutationRate;
        this.realDistribution = realDistribution;
        this.provideAtMostOfEach = provideAtMostOfEach;
        providedTillNow = new ArrayList<>(mutants.size());
        for (int i = 0; i < mutants.size(); i++) {
            providedTillNow.set(i, 0);
        }
        this.mutantDistribution = new UniformIntegerDistribution(0,this.mutants.size()-1);
    }

    @Override
    public Heuristic getNewHeuristic() {
        if(realDistribution.sample() < mutationRate ){
            int chosenMutant = mutantDistribution.sample() % this.mutants.size();
            providedTillNow.set(chosenMutant, providedTillNow.get(chosenMutant)+1);
            SelfLearningHeuristic result = mutants.get(chosenMutant).cleanCopy();
            if(providedTillNow.get(chosenMutant).equals(provideAtMostOfEach)){
                mutants.remove(chosenMutant);
                providedTillNow.remove(chosenMutant);
            }
            if(observable!=null){
                observable.addObserver(result);
            }
            return result;
        }
        return population.copy();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void setObservable(Observable observable) {
        this.observable = observable;
    }
}
